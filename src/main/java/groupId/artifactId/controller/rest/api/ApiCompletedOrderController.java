package groupId.artifactId.controller.rest.api;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.manager.api.ICompletedOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/completed_order")
public class ApiCompletedOrderController {
    private final ICompletedOrderManager completedOrderManager;

    @Autowired
    public ApiCompletedOrderController(ICompletedOrderManager completedOrderManager) {
        this.completedOrderManager = completedOrderManager;
    }

    //Read POSITION
    //1)  Read item need id param
    @GetMapping("/{id}")
    protected ResponseEntity<CompletedOrderDtoOutput> get(@PathVariable long id) {
        return ResponseEntity.ok(completedOrderManager.getAllData(id));
    }

    //Read POSITION
    //1)  Read item need ticket id param
    @GetMapping("ticket/{id}")
    protected ResponseEntity<CompletedOrderDtoOutput> getByTicket(@PathVariable long id) {
        return ResponseEntity.ok(completedOrderManager.getCompletedOrderByTicket(id));
    }

    //Read POSITION
    //1) Read list
    @GetMapping
    protected ResponseEntity<List<CompletedOrderDtoCrudOutput>> getList() {
        return ResponseEntity.ok(completedOrderManager.get());
    }
}