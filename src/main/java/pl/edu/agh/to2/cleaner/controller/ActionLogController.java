package pl.edu.agh.to2.cleaner.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.cleaner.service.ActionLogService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class ActionLogController {
    private final ActionLogService service;
    private static final int defaultLimit = 50;

    public ActionLogController(ActionLogService service) {
        this.service = service;
    }

    @GetMapping("/api/log")
    public List<ResponseItem> getLogs(@RequestParam("limit") Optional<Integer> optLimit) throws IOException {
        int limit = optLimit.orElse(defaultLimit);

        if (limit < 0) {
            limit = defaultLimit;
        }

        return service.lastNLogs(limit).stream().map(ResponseItem::createResponseItem).toList();
    }

    private record ResponseItem(String datetime, String method, String file1, String file2) {
        public static ResponseItem createResponseItem(String input) {
            input = input.stripTrailing();
            String[] parts = input.split("\\|", -1); // Use -1 to include empty strings

            return new ResponseItem(parts[0], parts[1], parts[2], parts[3]);
        }
    }


}
