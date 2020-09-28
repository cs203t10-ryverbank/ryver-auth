package cs203t10.ryver.auth.util;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class CustomBeanUtils extends BeanUtils {

    public static void copyNonNullProperties(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName ->
                        wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

}

