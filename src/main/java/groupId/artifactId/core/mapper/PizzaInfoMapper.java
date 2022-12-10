package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.PizzaInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PizzaInfoMapper {
    public PizzaInfoMapper() {
    }

    public PizzaInfo inputMapping(PizzaInfoDtoInput pizzaInfoDtoInput) {
        return PizzaInfo.builder()
                .name(pizzaInfoDtoInput.getName())
                .description(pizzaInfoDtoInput.getDescription())
                .size(pizzaInfoDtoInput.getSize()).build();
    }

    public PizzaInfoDtoOutput outputMapping(PizzaInfo pizzaInfo) {
        return PizzaInfoDtoOutput.builder()
                .name(pizzaInfo.getName())
                .description(pizzaInfo.getDescription())
                .size(pizzaInfo.getSize())
                .build();
    }
}
