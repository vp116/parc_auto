package com.angbe.soro.parc_auto.services;

import com.angbe.soro.parc_auto.models.Service;
import com.angbe.soro.parc_auto.repository.AppConfig;
import com.angbe.soro.parc_auto.repository.Repository;
import com.angbe.soro.parc_auto.repository.RepositoryFactory;

import java.util.List;
import java.util.Optional;

public class ServiceService {
    private final Repository<Service> serviceRepository;

    public ServiceService() {
        RepositoryFactory factory = AppConfig.getRepositoryFactory();
        this.serviceRepository = factory.getRepository(Service.class);
    }

    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }

    public Optional<Service> getServiceById(int id) {
        return serviceRepository.findById(id);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public void deleteService(int id) {
        serviceRepository.delete(id);
    }
}