package com.factory.stitch.controller;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
import com.factory.stitch.dto.ApiResponse;
import com.factory.stitch.dto.DepartmentSummary;
import com.factory.stitch.dto.ProcessResult;
import com.factory.stitch.service.DepartmentService;
import com.factory.stitch.service.RoleMatrixService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/erp")
@CrossOrigin(origins = "*")
public class ErpController {

    private final DepartmentService departmentService;
    private final RoleMatrixService roleMatrixService;

    public ErpController(DepartmentService departmentService, RoleMatrixService roleMatrixService) {
        this.departmentService = departmentService;
        this.roleMatrixService = roleMatrixService;
    }

    @GetMapping("/health")
    public ApiResponse health() {
        return new ApiResponse(true, "ERP backend aktif.");
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse> listDepartments(@RequestParam("role") String role) {
        if (!roleMatrixService.isKnownRole(role)) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "Gecersiz rol veya yetkisiz erisim."));
        }

        List<DepartmentSummary> departments = departmentService.getVisibleDepartments(role);
        return ResponseEntity.ok(new ApiResponse(true, "Departman listesi getirildi.", departments));
    }

    @GetMapping("/departments/{code}")
    public ResponseEntity<ApiResponse> departmentStatus(@PathVariable String code, @RequestParam("role") String role) {
        Optional<DepartmentSummary> summary = departmentService.getDepartmentStatus(role, code);
        if (summary.isEmpty()) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "Bu departman icin erisim yetkiniz yok."));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Departman durumu getirildi.", summary.get()));
    }

    @PostMapping("/departments/{code}/process")
    public ResponseEntity<ApiResponse> runProcess(@PathVariable String code, @RequestParam("role") String role) {
        Optional<ProcessResult> result = departmentService.runDepartmentProcess(role, code);
        if (result.isEmpty()) {
            return ResponseEntity.status(403).body(new ApiResponse(false, "Surec baslatilamadi. Yetki veya departman kontrolu basarisiz."));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Surec simulasyonu basariyla tamamlandi.", result.get()));
    }
}

