package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.dao.entity.Pizza;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PizzaMapperTest {
    @InjectMocks
    private PizzaMapper pizzaMapper;

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final String name = "ITALIANO PIZZA";
        final int size = 32;
        Pizza pizza = Pizza.builder().id(id).name(name).size(size).build();

        //test
        PizzaDtoOutput test = pizzaMapper.outputMapping(pizza);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(name, test.getName());
        Assertions.assertEquals(size, test.getSize());
    }
}