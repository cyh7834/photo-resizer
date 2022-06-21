package com.yoonho.photoresizer.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("code", 400);
                model.addAttribute("message", "Not a valid request.");
            }
            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("code", 404);
                model.addAttribute("message", "Page not found.");
            }
            else {
                model.addAttribute("code", 500);
                model.addAttribute("message", "Internal server error.");
            }

            return "error/error";
        }

        return "redirect:/";
    }
}
