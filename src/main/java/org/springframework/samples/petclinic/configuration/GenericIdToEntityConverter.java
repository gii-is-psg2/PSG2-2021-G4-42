package org.springframework.samples.petclinic.configuration;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.stereotype.Component;

@Component()
public final class GenericIdToEntityConverter implements ConditionalGenericConverter {
    private static final Logger log = LoggerFactory.getLogger(GenericIdToEntityConverter.class);

    private final ConversionService conversionService=new DefaultConversionService();
    
    @Autowired(required = false)
    private EntityManager entityManager;

        

    @Override
	public Set<ConvertiblePair> getConvertibleTypes() {
    	final Set<ConvertiblePair> result=new HashSet<>();
        result.add(new ConvertiblePair(Number.class, BaseEntity.class));
        result.add(new ConvertiblePair(CharSequence.class, BaseEntity.class));
        return result;
    }

    @Override
	public boolean matches(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        return BaseEntity.class.isAssignableFrom(targetType.getType())
        && this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(Integer.class));
    }

    @Override
	public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        if (source == null || this.entityManager==null) {
            return null;
        }

        final Integer id = (Integer) this.conversionService.convert(source, sourceType, TypeDescriptor.valueOf(Integer.class));

        final Object entity = this.entityManager.find(targetType.getType(), id);
        if (entity == null) {
            GenericIdToEntityConverter.log.info("Did not find an entity with id {} of type {}", id,  targetType.getType());
            return null;
        }

        return entity;
    }

}
