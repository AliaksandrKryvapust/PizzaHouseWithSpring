package groupId.artifactId.manager;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.core.mapper.SelectedItemMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.service.MenuItemService;
import groupId.artifactId.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

class OrderManagerTest {
    @InjectMocks
    private OrderManager orderManager;
    @Mock
    private OrderService orderService;
    @Mock
    private MenuItemService menuItemService;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private SelectedItemMapper selectedItemMapper;

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final int count = 5;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String pizzaDescription = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final List<Long> ids = singletonList(id);
        final List<SelectedItemDtoInput> selectedItemDtoInput = singletonList(SelectedItemDtoInput.builder()
                .menuItemId(id).count(count).build());
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(pizzaDescription).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).count(count).menuItem(menuItem)
                .createAt(creationDate).build());
        final OrderDtoInput orderDtoInput = OrderDtoInput.builder().selectedItems(selectedItemDtoInput).build();
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final TicketDtoCrudOutput crudOutput = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate)
                .build();
        Mockito.when(menuItemService.getListById(ids)).thenReturn(singletonList(menuItem));
        Mockito.when(selectedItemMapper.inputMapping(any(SelectedItemDtoInput.class), any())).thenReturn(selectedItems.get(0));
        Mockito.when(orderService.save(any(ITicket.class))).thenReturn(ticket);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(crudOutput);

        //test
        TicketDtoCrudOutput test = orderManager.save(orderDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final Instant creationDate = Instant.now();
        List<ITicket> tickets = singletonList(Ticket.builder().id(id).createAt(creationDate).build());
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate).build();
        Mockito.when(orderService.get()).thenReturn(tickets);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(output);

        //test
        List<TicketDtoCrudOutput> test = orderManager.get();

        // assert
        Assertions.assertEquals(tickets.size(), test.size());
        for (TicketDtoCrudOutput ticket : test) {
            Assertions.assertNotNull(ticket);
            Assertions.assertEquals(id, ticket.getId());
            Assertions.assertEquals(id, ticket.getOrderId());
            Assertions.assertEquals(creationDate, ticket.getCreateAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final Instant creationDate = Instant.now();
        final ITicket ticket = Ticket.builder().id(id).createAt(creationDate).build();
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate).build();
        Mockito.when(orderService.get(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(output);

        //test
        TicketDtoCrudOutput test = orderManager.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
    }

    @Test
    void getAllData() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        Mockito.when(orderService.get(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(ticketDtoOutput);

        //test
        TicketDtoOutput test = orderManager.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getOrder().getId());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreatedAt());
            Assertions.assertEquals(version, output.getMenuItem().getVersion());
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
        }
    }
}