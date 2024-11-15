package com.buenrostroasociados.gestion_clientes.service.publico;

import com.buenrostroasociados.gestion_clientes.dto.PublicInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PublicInfoService {

    @Value("${api.releaseDate}")
    private String releaseDateString;

    public PublicInfoDTO obtenerInformacionPublica() {
        // Convertimos el String a LocalDate
        LocalDate releaseDate = LocalDate.parse(releaseDateString);
        return PublicInfoDTO.builder()
                .nombre("Gestión de Clientes Buenrostro Asociados")
                .descripcion("En Buenrostro Asociados nos especializamos en ofrecer servicios de litigo y contabilidad a empresas y particulares. "
                        + "Nuestra misión es brindar soluciones legales y financieras de alta calidad, con un enfoque personalizado y profesional para cada cliente.")
                .version("1.0.0")
                .contacto("contacto@buenrostroasociados.com")
                .whatsappLink("https://wa.me/1234567890")
                .facebookLink("https://www.facebook.com/BuenrostroAsociados")
                .instagramLink("https://www.instagram.com/BuenrostroAsociados")
                .servicios("Nuestros servicios incluyen:\n"
                        + "- Asesoría legal en litigios.\n"
                        + "- Contabilidad empresarial y personal.\n"
                        + "- Elaboración de informes financieros con respectoa al actividad.\n"
                        + "- Consultoría en temas fiscales y tributarios.\n"
                        + "Nos especializamos en ofrecer soluciones que se adaptan a las necesidades específicas de cada cliente, "
                        + "con un equipo de profesionales comprometidos con la excelencia.")
                .releaseDate(releaseDate)  // Establece la fecha de lanzamiento como la fecha actual
                .build();
    }

}
