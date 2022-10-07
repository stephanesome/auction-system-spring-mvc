package seg3x02.auctionsystem.infrastructure.web.controllers

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import seg3x02.auctionsystem.infrastructure.web.forms.SearchRequest
import javax.servlet.http.HttpSession

@Controller
class ApplicationErrorController: ErrorController {
    @GetMapping("/error")
    fun showError(model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        return "error"
    }
}
