package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Utilisateur;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;
import com.angbe.soro.parc_auto.repository.UnitOfWork;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class AuthenticationService {
    private final Repository<Utilisateur> utilisateurRepository;
    private Utilisateur utilisateurConnecte;

    public AuthenticationService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.utilisateurRepository = factory.getRepository(Utilisateur.class);
    }

    public boolean authentifier(String email, String motDePasse) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.query(em -> {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream()
                    .findFirst();
        });

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();

            if (!utilisateur.isActif()) {
                return false;
            }

            String motDePasseHash = hashMotDePasse(motDePasse);
            if (motDePasseHash.equals(utilisateur.getMotDePasse())) {
                this.utilisateurConnecte = utilisateur;
                return true;
            }
        }

        return false;
    }

    public Utilisateur inscrire(String nom, String prenom, String email, String motDePasse, String role) {
        boolean emailExiste = utilisateurRepository.query(em -> {
            return em.createQuery("SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult() > 0;
        });

        if (emailExiste) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(hashMotDePasse(motDePasse));
        utilisateur.setRole(role != null ? role : "UTILISATEUR");
        utilisateur.setActif(true);

        utilisateurRepository.save(utilisateur);

        return utilisateur;
    }

    public void deconnecter() {
        this.utilisateurConnecte = null;
    }

    public boolean estConnecte() {
        return this.utilisateurConnecte != null;
    }

    public Utilisateur getUtilisateurConnecte() {
        if (!estConnecte()) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }
        return this.utilisateurConnecte;
    }

    public boolean hasRole(String role) {
        return estConnecte() && role != null && role.equals(utilisateurConnecte.getRole());
    }

    public boolean changerMotDePasse(String email, String ancienMotDePasse, String nouveauMotDePasse) {
        if (email == null || ancienMotDePasse == null || nouveauMotDePasse == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.query(em -> {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream()
                    .findFirst();
        });

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            String ancienMotDePasseHash = hashMotDePasse(ancienMotDePasse);

            if (ancienMotDePasseHash.equals(utilisateur.getMotDePasse())) {
                utilisateur.setMotDePasse(hashMotDePasse(nouveauMotDePasse));
                utilisateurRepository.save(utilisateur);

                return true;
            }
        }

        return false;
    }

    public boolean reinitialiserMotDePasse(String email, String nouveauMotDePasse) {
        if (email == null || nouveauMotDePasse == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.query(em -> {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getResultList()
                    .stream()
                    .findFirst();
        });

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setMotDePasse(hashMotDePasse(nouveauMotDePasse));
            utilisateurRepository.save(utilisateur);
            return true;
        }

        return false;
    }

    private String hashMotDePasse(String motDePasse) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }

    public void desactiverUtilisateur(int idUtilisateur) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(idUtilisateur);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setActif(false);
            utilisateurRepository.save(utilisateur);
        }
    }

    public void activerUtilisateur(int idUtilisateur) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(idUtilisateur);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setActif(true);
            utilisateurRepository.save(utilisateur);
        }
    }
}