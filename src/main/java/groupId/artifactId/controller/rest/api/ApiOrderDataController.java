package groupId.artifactId.controller.rest.api;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.manager.api.IOrderDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/order_data")
public class ApiOrderDataController {
    private final IOrderDataManager orderDataManager;

    @Autowired
    public ApiOrderDataController(IOrderDataManager orderDataManager) {
        this.orderDataManager = orderDataManager;
    }

    //Read POSITION
    //1) Read item need id param
    @GetMapping("/{id}")
    protected ResponseEntity<OrderDataDtoOutput> get(@PathVariable long id) {
        return ResponseEntity.ok(orderDataManager.getAllData(id));
    }

    //Read POSITION
    //1) Read item by ticket
    @GetMapping("/ticket/{id}")
    protected ResponseEntity<OrderDataDtoOutput> getByTicket(@PathVariable long id) {
        return ResponseEntity.ok(orderDataManager.getOrderDataByTicket(id));
    }

    //Read POSITION
    //1) Read list
    @GetMapping
    protected ResponseEntity<List<OrderDataDtoCrudOutput>> getList() {
        return ResponseEntity.ok(orderDataManager.get());
    }

    //CREATE POSITION
    //body json
    @PostMapping
    protected ResponseEntity<OrderDataDtoCrudOutput> post(@Valid @RequestBody OrderDataDtoInput orderDataDtoInput) {
        return ResponseEntity.ok(orderDataManager.save(orderDataDtoInput));
    }
}
