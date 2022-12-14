package groupId.artifactId.manager;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.manager.api.IOrderDataManager;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataManager implements IOrderDataManager {
    private final IOrderDataService orderDataService;
    private final IOrderService orderService;
    private final OrderDataMapper orderDataMapper;

    @Autowired
    public OrderDataManager(IOrderDataService orderDataService, IOrderService orderService,
                            OrderDataMapper orderDataMapper) {
        this.orderDataService = orderDataService;
        this.orderService = orderService;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public OrderDataDtoCrudOutput save(OrderDataDtoInput orderDataDtoInput) {
        Ticket ticket = this.orderService.get(orderDataDtoInput.getTicketId());
        OrderDataDtoInput orderData = OrderDataDtoInput.builder().ticketId(orderDataDtoInput.getTicketId())
                .description(orderDataDtoInput.getDescription()).ticket(ticket).build();
        OrderData orderDataOutput = this.orderDataService.saveInTransaction(orderData);
        return orderDataMapper.outputCrudMapping(orderDataOutput);
    }

    @Override
    public List<OrderDataDtoCrudOutput> get() {
        return this.orderDataService.get().stream().map(orderDataMapper::outputCrudMapping).collect(Collectors.toList());
    }

    @Override
    public OrderDataDtoCrudOutput get(Long id) {
        return orderDataMapper.outputCrudMapping(this.orderDataService.get(id));
    }

    @Override
    public OrderDataDtoOutput getAllData(Long id) {
        return orderDataMapper.outputMapping(this.orderDataService.get(id));
    }

    @Override
    public OrderDataDtoOutput getOrderDataByTicket(Long id) {
        return orderDataMapper.outputMapping(this.orderDataService.findOrderDataByTicketId(id));
    }
}
