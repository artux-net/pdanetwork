package net.artux.pdanetwork.models.error;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ErrorMapper {

    ErrorResponse of(String details, String title);

    @Mapping(target = "details", source = "ex.localizedMessage")
    ErrorResponse of(Exception ex, String title);

    @Mapping(target = "details", source = "ex.bindingResult.allErrors")
    ErrorResponse of(BindException ex, String title);

    default List<ErrorModel> models(String message) {
        return Collections.singletonList(new ErrorModel(null, message));
    }

    default List<ErrorModel> models(List<ObjectError> objectErrors) {
        List<ErrorModel> errorModels = new LinkedList<>();
        for (ObjectError objectError : objectErrors) {
            if (objectError instanceof FieldError fieldError) {
                errorModels.add(new ErrorModel(fieldError.getField(), fieldError.getDefaultMessage()));
            }
        }
        return errorModels;
    }
}
