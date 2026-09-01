package com.fastcam.springserver.controller;
import com.fastcam.springserver.dto.ItemUseRequest;
import com.fastcam.springserver.service.AffinityService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/affinity")
public class AffinityController {
    private final AffinityService service;
    public AffinityController(AffinityService service){this.service=service;}
    @GetMapping("/myInfo") public Map<String,Object> myInfo(@RequestParam int userId){return service.myInfo(userId);}
    @PostMapping("/useItem") public Map<String,Object> useItem(@RequestBody ItemUseRequest request){return service.useItem(request.getUserId(),request.getItemId());}
    @GetMapping("/ranking") public Map<String,Object> ranking(){List<Map<String,Object>> rows=service.ranking();return Map.of("ranking",rows,"count",rows.size());}
}
