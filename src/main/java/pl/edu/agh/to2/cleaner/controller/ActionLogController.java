package pl.edu.agh.to2.cleaner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.cleaner.service.ActionLogService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/log")
public class ActionLogController {
    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 500;
    private final ActionLogService service;

    public ActionLogController(ActionLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getLogs(@RequestParam(value = "limit", required = false) Optional<Integer> optLimit) {
        try {
            int limit = optLimit.orElse(DEFAULT_LIMIT);

            if (limit <= 0) {
                limit = DEFAULT_LIMIT;
            } else if (limit > MAX_LIMIT) {
                limit = MAX_LIMIT;
            }

            List<ResponseItem> logs = service.lastNLogs(limit).stream().map(ResponseItem::createResponseItem).toList();

            if (logs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            log.error("Unexpected error in log retrieval", e);
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    private record ResponseItem(String datetime, String method, String file1, String file2) {
        public static ResponseItem createResponseItem(String input) {
            input = input.stripTrailing();
            String[] parts = input.split("\\|", -1);
            if (parts.length < 4) {
                throw new IllegalArgumentException("Invalid log format: " + input);
            }

            return new ResponseItem(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        }
    }
}