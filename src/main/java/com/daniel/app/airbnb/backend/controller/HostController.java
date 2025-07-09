package com.daniel.app.airbnb.backend.controller;


import com.daniel.app.airbnb.backend.dto.BecomeHostDto;
import com.daniel.app.airbnb.backend.res.HttpResponse;
import com.daniel.app.airbnb.backend.service.BecomeHostService;
import com.daniel.app.airbnb.backend.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HostController extends  BaseController{

    private  final BecomeHostService becomeHostService;
    @PostMapping("host")
    public ResponseEntity<HttpResponse<Object>> becomeHost(@Valid @RequestBody BecomeHostDto becomeHostDto){
        var host =  becomeHostService.becomeHost(becomeHostDto);
        return  ResponseEntity.ok(ResponseUtil.getResponse(null,null,host
                , HttpStatus.OK));
    }

}
