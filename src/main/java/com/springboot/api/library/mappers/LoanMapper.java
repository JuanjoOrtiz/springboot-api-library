package com.springboot.api.library.mappers;

import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import com.springboot.api.library.entities.Loan;
import com.springboot.api.library.entities.Loan;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LoanMapper {
    @Mapping(source = "checkoutDate", target = "checkoutDate", dateFormat = "dd/MM/yyyy HH:mm:ss")
    @Mapping(source = "dueDate", target = "dueDate", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "returnDate", target = "returnDate", dateFormat = "dd/MM/yyyy HH:mm:ss") LoanResponseDTO toDTO(Loan entity);

    @Mapping(target = "id", ignore = true)
    public Loan toEntity(LoanRequestDTO requestDTO);


    public void updateEntityFromDto(LoanRequestDTO requestDTO,@MappingTarget Loan entity);
}
