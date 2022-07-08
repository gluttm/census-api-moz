package mz.co.truetech.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ZoneConverter implements AttributeConverter<Zone, String> {
 
    @Override
    public String convertToDatabaseColumn(Zone zone) {
        if (zone == null) {
            return null;
        }
        return zone.getCode();
    }

    @Override
    public Zone convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Zone.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}