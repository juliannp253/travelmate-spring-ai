package com.todocodeacademy.travelmate.service.impl;
import com.todocodeacademy.travelmate.service.interfaces.IDestinoService;

import com.todocodeacademy.travelmate.dto.DestinoRequest;
import com.todocodeacademy.travelmate.exception.RecursoNoEncontradoException;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.enums.TipoDestino;
import com.todocodeacademy.travelmate.repository.DestinoRepository;
import org.springframework.stereotype.Service;
import com.todocodeacademy.travelmate.ai.index.CatalogChangePublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DestinoService implements IDestinoService {

    private final DestinoRepository destinoRepository;
    private final CatalogChangePublisher catalogChanges;

    public DestinoService(DestinoRepository destinoRepository, CatalogChangePublisher catalogChanges) {
        this.destinoRepository = destinoRepository;
        this.catalogChanges = catalogChanges;
    }

    @Override
    public List<Destino> listar() {
        return destinoRepository.findAll();
    }

    @Override
    public Destino buscarPorId(Long id) {
        return destinoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el destino con ID " + id
                ));
    }

    @Override
    public List<Destino> buscarPorPais(String pais) {
        return destinoRepository.findByPaisIgnoreCase(pais);
    }

    @Override
    public List<Destino> buscarPorTipo(TipoDestino tipo) {
        return destinoRepository.findByTipo(tipo);
    }

    @Override
    public List<Destino> listarTranquilos() {
        return destinoRepository.findByTranquiloTrue();
    }

    @Override
    public List<Destino> listarFamiliares() {
        return destinoRepository.findByAptoFamiliasTrue();
    }

    @Override
    public List<Destino> listarParaParejas() {
        return destinoRepository.findByAptoParejasTrue();
    }

    @Override
    @Transactional
    public Destino crear(DestinoRequest request) {
        Destino destino = new Destino(
                request.nombre(),
                request.pais(),
                request.ciudad(),
                request.tipo(),
                request.descripcion(),
                request.temperaturaPromedio(),
                request.aptoFamilias(),
                request.aptoParejas(),
                request.tranquilo(),
                request.mejorEpoca()
        );

        Destino guardado = destinoRepository.save(destino);
        catalogChanges.destino(guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public Destino actualizar(Long id, DestinoRequest request) {
        Destino destino = buscarPorId(id);

        destino.setNombre(request.nombre());
        destino.setPais(request.pais());
        destino.setCiudad(request.ciudad());
        destino.setTipo(request.tipo());
        destino.setDescripcion(request.descripcion());
        destino.setTemperaturaPromedio(request.temperaturaPromedio());
        destino.setAptoFamilias(request.aptoFamilias());
        destino.setAptoParejas(request.aptoParejas());
        destino.setTranquilo(request.tranquilo());
        destino.setMejorEpoca(request.mejorEpoca());

        Destino guardado = destinoRepository.save(destino);
        catalogChanges.destino(guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Destino destino = buscarPorId(id);

        catalogChanges.destino(id);
        destinoRepository.delete(destino);
    }
}
