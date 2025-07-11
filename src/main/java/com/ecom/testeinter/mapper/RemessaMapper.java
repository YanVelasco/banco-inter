package com.ecom.testeinter.mapper;

import com.ecom.testeinter.dto.RemessaRequestDTO;
import com.ecom.testeinter.dto.RemessaResponseDTO;
import com.ecom.testeinter.model.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RemessaMapper {

    public RemessaResponseDTO toResponseDTO(RemessaRequestDTO requestDTO, double valorConvertidoDolares, Usuario remetente, Usuario destinatario) {
        RemessaResponseDTO responseDTO = new RemessaResponseDTO();
        responseDTO.setRemetenteId(remetente.getId());
        responseDTO.setDestinatarioId(destinatario.getId());
        responseDTO.setValorConvertidoDolares(BigDecimal.valueOf(valorConvertidoDolares).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return responseDTO;
    }
}
