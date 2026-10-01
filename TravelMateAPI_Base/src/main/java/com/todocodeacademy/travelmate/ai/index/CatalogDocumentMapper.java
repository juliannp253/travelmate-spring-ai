package com.todocodeacademy.travelmate.ai.index;

import com.todocodeacademy.travelmate.model.Actividad;
import com.todocodeacademy.travelmate.model.Destino;
import com.todocodeacademy.travelmate.model.Hotel;
import com.todocodeacademy.travelmate.model.PaqueteTuristico;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.UUID;

@Component
public class CatalogDocumentMapper {

    public Document toDocument(PaqueteTuristico paquete) {
        Assert.notNull(paquete, "El paquete no puede ser null");
        Assert.notNull(paquete.getId(), "El paquete debe estar guardado");
        Assert.notNull(paquete.getDestino(), "El paquete debe tener destino");
        Assert.notNull(paquete.getHotel(), "El paquete debe tener hotel");

        Destino destino = paquete.getDestino();
        Hotel hotel = paquete.getHotel();
        StringBuilder contenido = new StringBuilder();

        agregar(contenido, "Paquete ID", paquete.getId());
        agregar(contenido, "Nombre", paquete.getNombre());
        agregar(contenido, "Descripción del paquete", paquete.getDescripcion());
        agregar(contenido, "Precio por persona", paquete.getPrecioPorPersona());
        agregar(contenido, "Duración en días", paquete.getCantidadDias());
        agregar(contenido, "Transporte", paquete.getTipoTransporte());
        agregar(contenido, "Incluye traslado", paquete.getIncluyeTraslado());
        agregar(contenido, "Incluye desayuno", paquete.getIncluyeDesayuno());
        agregar(contenido, "Incluye excursiones", paquete.getIncluyeExcursiones());

        agregar(contenido, "Destino ID", destino.getId());
        agregar(contenido, "Destino", destino.getNombre());
        agregar(contenido, "País", destino.getPais());
        agregar(contenido, "Ciudad", destino.getCiudad());
        agregar(contenido, "Tipo de destino", destino.getTipo());
        agregar(contenido, "Descripción del destino", destino.getDescripcion());
        agregar(contenido, "Temperatura promedio", destino.getTemperaturaPromedio());
        agregar(contenido, "Destino apto para familias", destino.getAptoFamilias());
        agregar(contenido, "Destino apto para parejas", destino.getAptoParejas());
        agregar(contenido, "Destino tranquilo", destino.getTranquilo());
        agregar(contenido, "Mejor época", destino.getMejorEpoca());

        agregar(contenido, "Hotel ID", hotel.getId());
        agregar(contenido, "Hotel", hotel.getNombre());
        agregar(contenido, "Estrellas", hotel.getEstrellas());
        agregar(contenido, "Precio del hotel por noche", hotel.getPrecioNoche());
        agregar(contenido, "Hotel ofrece desayuno", hotel.getDesayunoIncluido());
        agregar(contenido, "Hotel con piscina", hotel.getPileta());
        agregar(contenido, "Hotel con spa", hotel.getSpa());
        agregar(contenido, "Hotel apto para niños", hotel.getAptoNinos());

        if (paquete.getActividades() == null
                || paquete.getActividades().isEmpty()) {
            agregar(contenido, "Actividades asociadas", "Ninguna");
        } else {
            paquete.getActividades().stream()
                    .sorted(Comparator.comparing(
                            Actividad::getId,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .forEach(actividad -> {
                        contenido.append("\nActividad asociada:\n");
                        agregar(contenido, "Actividad ID", actividad.getId());
                        agregar(contenido, "Nombre", actividad.getNombre());
                        agregar(contenido, "Tipo", actividad.getTipo());
                        agregar(contenido, "Descripción", actividad.getDescripcion());
                        agregar(contenido, "Duración en horas", actividad.getDuracionHoras());
                        agregar(contenido, "Precio de la actividad", actividad.getPrecio());
                        agregar(contenido, "Apta para niños", actividad.getAptaNinos());
                    });
        }

        return Document.builder()
                .id(documentId(paquete.getId()))
                .text(contenido.toString().strip())
                .metadata("paqueteId", paquete.getId())
                .build();
    }

    public String documentId(Long paqueteId) {
        Assert.notNull(paqueteId, "El ID del paquete es obligatorio");

        String clave = "travelmate:paquete:" + paqueteId;

        return UUID.nameUUIDFromBytes(
                clave.getBytes(StandardCharsets.UTF_8)
        ).toString();
    }

    private void agregar(StringBuilder contenido, String etiqueta, Object valor) {
        contenido.append(etiqueta)
                .append(": ")
                .append(texto(valor))
                .append('\n');
    }

    private String texto(Object valor) {
        if (valor == null) {
            return "No informado";
        }

        if (valor instanceof Boolean booleano) {
            return booleano ? "Sí" : "No";
        }

        if (valor instanceof BigDecimal numero) {
            return numero.stripTrailingZeros().toPlainString();
        }

        String resultado = valor.toString()
                .replaceAll("\\s+", " ")
                .strip();

        return resultado.isEmpty() ? "No informado" : resultado;
    }
}
