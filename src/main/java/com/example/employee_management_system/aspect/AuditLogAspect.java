package com.example.employee_management_system.aspect;

import com.example.employee_management_system.model.AuditLog;
import com.example.employee_management_system.repository.AuditLogRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLogAspect {

    @Autowired
    private AuditLogRepo auditLogRepo;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* com.example.employee_management_system.service.*.save*(..))")
    public void createEntity() {}

    @Pointcut("execution(* com.example.employee_management_system.service.*.update*(..))")
    public void updateEntity() {}

    @Pointcut("execution(* com.example.employee_management_system.service.*.delete*(..))")
    public void deleteEntity() {}

    @AfterReturning(value = "createEntity()", returning = "entity")
    public void logCreate(JoinPoint joinPoint, Object entity) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        logAction("CREATE", entity);
    }

    @AfterReturning(value = "updateEntity()", returning = "entity")
    public void logUpdate(JoinPoint joinPoint, Object entity) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        logAction("UPDATE", entity);
    }

    @AfterReturning("deleteEntity() && args(id)")
    public void logDelete(JoinPoint joinPoint, Long id) {
        String entityName = joinPoint.getTarget().getClass().getSimpleName().replace("Service", "");
        logAction("DELETE", entityName, id);
    }

    private void logAction(String action, Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (entity != null) {
            String entityName = entity.getClass().getSimpleName();
            Long entityId = (Long) entity.getClass().getMethod("getId").invoke(entity);
            logAction(action, entityName, entityId);
        }
    }

    private void logAction(String action, String entityName, Long entityId) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(request.getRemoteUser());
        auditLogRepo.save(log);
    }
}
