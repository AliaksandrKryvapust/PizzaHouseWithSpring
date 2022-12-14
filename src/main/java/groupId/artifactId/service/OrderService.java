package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static groupId.artifactId.core.Constants.ORDER_START_DESCRIPTION;

@Service
public class OrderService implements IOrderService {
    private final ITicketDao ticketDao;
    private final IOrderDataService orderDataService;

    @Autowired
    public OrderService(ITicketDao ticketDao, IOrderDataService orderDataService) {
        this.ticketDao = ticketDao;
        this.orderDataService = orderDataService;
    }

    @Override
    @Transactional
    public Ticket save(Ticket ticket) {
        if (ticket.getId() != null) {
            throw new IllegalStateException("Ticket id should be empty");
        }
        Ticket savedTicket = this.ticketDao.save(ticket);
        OrderDataDtoInput dtoInput = OrderDataDtoInput.builder().ticketId(savedTicket.getId())
                .description(ORDER_START_DESCRIPTION).ticket(savedTicket).build();
        orderDataService.create(dtoInput);
        return savedTicket;
    }

    @Override
    public List<Ticket> get() {
        return this.ticketDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket get(Long id) {
        return this.ticketDao.findById(id).orElseThrow();
    }
}
