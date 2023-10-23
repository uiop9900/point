package com.jia.point.common.converter;

import io.netty.util.internal.ObjectUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jdk.jshell.execution.LoaderDelegate;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        if (ObjectUtils.isEmpty(localDate)) {
            return null;
        }
        return localDate.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (ObjectUtils.isEmpty(dbData)) {
            return null;
        }
        return LocalDate.parse(dbData);
    }
}
