package groupId.artifactId.manager;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.manager.api.ICompletedOrderManager;
import groupId.artifactId.service.api.ICompletedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompletedOrderManager implements ICompletedOrderManager {
    private final ICompletedOrderService completedOrderService;
    private final CompletedOrderMapper completedOrderMapper;

    @Autowired
    public CompletedOrderManager(ICompletedOrderService completedOrderService, CompletedOrderMapper completedOrderMapper) {
        this.completedOrderService = completedOrderService;
        this.completedOrderMapper = completedOrderMapper;
    }

    @Override
    public CompletedOrderDtoOutput getAllData(Long id) {
        return completedOrderMapper.outputMapping(this.completedOrderService.get(id));
    }

    @Override
    public CompletedOrderDtoOutput getCompletedOrderByTicket(Long id) {
        return completedOrderMapper.outputMapping(this.completedOrderService.findCompletedOrderByTicketId(id));
    }

    @Override
    public CompletedOrderDtoCrudOutput save(CompletedOrder type) {
        CompletedOrder completedOrder = this.completedOrderService.save(type);
        return this.completedOrderMapper.outputCrudMapping(completedOrder);
    }

    @Override
    public List<CompletedOrderDtoCrudOutput> get() {
        return this.completedOrderService.get().stream().map(completedOrderMapper::outputCrudMapping)
                .collect(Collectors.toList());
    }

    @Override
    public CompletedOrderDtoCrudOutput get(Long id) {
        return completedOrderMapper.outputCrudMapping(this.completedOrderService.get(id));
    }
}
