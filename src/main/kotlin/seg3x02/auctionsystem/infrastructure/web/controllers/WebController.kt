package seg3x02.auctionsystem.infrastructure.web.controllers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import seg3x02.auctionsystem.application.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.application.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.infrastructure.web.forms.AccountForm
import seg3x02.auctionsystem.infrastructure.web.forms.AuctionForm
import seg3x02.auctionsystem.infrastructure.web.forms.BidForm
import seg3x02.auctionsystem.infrastructure.web.forms.SearchRequest
import seg3x02.auctionsystem.infrastructure.web.services.AuctionService
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.math.BigDecimal
import java.security.Principal
import java.util.*
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid

@Controller
@SessionAttributes(names = ["searchRequest"])
class WebController(private val auctionService: AuctionService) {
    @Value("classpath:static/images/defaultImage.jpg")
    lateinit var defaultImgResource: Resource

    @ModelAttribute("searchRequest")
    fun searchRequest(): SearchRequest {
        return SearchRequest()
    }


    @RequestMapping("/")
    fun showWelcome(model: Model, session: HttpSession): String {
        val auctions = auctionService.findAuctions("")
        session.setAttribute("auctions", auctions)
        model.addAttribute("auctions", auctions)
        return "welcome"
    }

    @GetMapping("/login")
    fun login(model: Model, session: HttpSession): String {
        return "login"
    }

    @PostMapping(value = ["/search"])
    fun auctionSearch(@ModelAttribute("searchRequest") searchRequest: SearchRequest,
                      model: Model, session: HttpSession): String {
        val auctions = auctionService.findAuctions(searchRequest.category)
        session.setAttribute("auctions", auctions)
        model.addAttribute("auctions", auctions)
        return "browseAuctions"
    }

    @GetMapping(value = ["/search"])
    fun showSearchResult(model: Model, session: HttpSession): String {
        val auctions = session.getAttribute("auctions") ?: ArrayList<AuctionBrowseDto>()
        model.addAttribute("auctions", auctions)
        return "browseAuctions"
    }

    @GetMapping("/auction")
    fun showAuction(
        @RequestParam selectedAuction: Int,
        model: Model, session: HttpSession
    ): String {
        val auctions = session.getAttribute("auctions") as List<AuctionBrowseDto>?
        return if (auctions != null &&
            (selectedAuction >= 0 && selectedAuction < auctions.size)
        ) {
            val auction = auctions?.get(selectedAuction)
            model.addAttribute("auction", auction)
            model.addAttribute("bidForm", BidForm())
            model.addAttribute("bidResult", "")
            session.setAttribute("selected", auction)
            "auction"
        } else {
            "error"
        }
    }

    @PostMapping(value = ["/auth/placeBid"])
    fun placeBid(bidForm: BidForm, model: Model, session: HttpSession, principal: Principal?): String {
        if (principal == null) {
            return "login"
        }
        val auction = session.getAttribute("selected") as AuctionBrowseDto
        model.addAttribute("auction", auction)
        if (auction.currentMinBid > BigDecimal(bidForm.amount)) {
            model.addAttribute("bidResult", "lowAmount")
        } else {
            // val account = session.getAttribute("currentUser") as AccountViewDto
            val account = getCurrentUser(session, principal)
            if (account != null) {
                if (account.userName == auction.sellerId) {
                    model.addAttribute("bidResult", "seller-bid")
                } else {
                    // call placebid with current user
                    if (auctionService.placeBid(account.userName, auction.id, bidForm.amount)) {
                        model.addAttribute("bidResult", "ok")
                    } else {
                        model.addAttribute("bidResult", "error")
                    }
                }
            }
        }
        return "auction"
    }

    @GetMapping(value = ["/register"])
    fun register(model: Model, session: HttpSession): String {
        val accountData = AccountForm()
        model.addAttribute("accountData", accountData)
        model.addAttribute("ccardErrors", false)
        return "createAccount"
    }

    @PostMapping(value = ["/register"])
    fun createAccount(
        @Valid @ModelAttribute("accountData") accountData: AccountForm, bindingResults: BindingResult,
        model: Model, session: HttpSession
    ): String {
        model.addAttribute("accountData", accountData)
        model.addAttribute("ccardErrors", creditCardFormErrors(bindingResults))
        if (!bindingResults.hasErrors()) {
            if (auctionService.createAccount(accountData)) {
                model.addAttribute("createAccountStatus", "ok")
                model.addAttribute("accountData", AccountForm())
            } else {
                model.addAttribute("createAccountStatus", "error")
            }
        }
        return "createAccount"
    }

    @GetMapping(value = ["/auth/account"])
    fun viewAccount(principal: Principal, model: Model, session: HttpSession): String {
        setAccountViewModel(session, model, principal)
        return "account"
    }

    @GetMapping(value = ["/auth/updateAccount"])
    fun updateAccount(principal: Principal, model: Model, session: HttpSession): String {
        val account = session.getAttribute("currentUser") as AccountViewDto
        val accountData = auctionService.setAccountForm(account)
        accountData.password = "xxxx"
        accountData.passwordConf = "xxxx"
        model.addAttribute("accountData", accountData)
        model.addAttribute("ccardErrors", false)
        return "updateAccount"
    }

    @PostMapping(value = ["/auth/updateAccount"])
    fun doUpdateAccount(
        @Valid @ModelAttribute("accountData") accountData: AccountForm,
        bindingResults: BindingResult,
        model: Model, session: HttpSession
    ): String {
        model.addAttribute("ccardErrors", creditCardFormErrors(bindingResults))
        // call service to update - pass account and accountData
        if (!bindingResults.hasErrors()) {
            var account = session.getAttribute("currentUser") as AccountViewDto
            auctionService.updateAccount(account, accountData)
        }
        model.addAttribute("accountData", accountData)
        return "updateAccount"
    }

    @GetMapping(value = ["/auth/deactivateAccount"])
    fun deactivateAccount(principal: Principal, model: Model, session: HttpSession): ModelAndView {
        val account = session.getAttribute("currentUser") as AccountViewDto?
        return if (account != null && auctionService.deactivate(account.userName)) {
            ModelAndView("redirect:/logout")
        } else {
            model.addAttribute("deactivateAccountStatus", "error")
            setAccountViewModel(session, model, principal)
            ModelAndView("account")
        }
    }

    private fun setAccountViewModel(
        session: HttpSession,
        model: Model,
        principal: Principal
    ) {
        var account = getCurrentUser(session, principal)
        model.addAttribute("account", account)
    }

    private fun getCurrentUser(session: HttpSession, principal: Principal): AccountViewDto? {
        var account = session.getAttribute("currentUser")
        if (account == null) {
            val userid = principal.name
            account = auctionService.getAccount(userid)
            session.setAttribute("currentUser", account)
        }
        return account as AccountViewDto
    }

    @GetMapping(value = ["/auth/newAuction"])
    fun newAuction(model: Model, session: HttpSession): String {
        model.addAttribute("createAuctionStatus", null)
        val auctionData = AuctionForm()
        model.addAttribute("auctionData", auctionData)
        model.addAttribute("ccardErrors", false)
        return "newAuction"
    }

    @PostMapping(value = ["/auth/createAuction"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addAuction(
        @Valid @ModelAttribute("auctionData") auctionData: AuctionForm, bindingResults: BindingResult,
        model: Model, session: HttpSession
    ): String {
        model.addAttribute("ccardErrors", creditCardFormErrors(bindingResults))
        val acc = session.getAttribute("currentUser")
        if (acc != null) {
            val account = acc as AccountViewDto
            if (account.pendingPayment > 0.0) {
                model.addAttribute("createAuctionStatus", "pp-error")
            } else if (account.creditCardNumber == null &&
                (auctionData.number == null || auctionData.number!!.trim()!!.isEmpty())
            ) {
                model.addAttribute("createAuctionStatus", "cc-error")
            } else if (!bindingResults.hasErrors()) {
                // create the auction here
                if (auctionService.createAuction(account, auctionData)) {
                    model.addAttribute("createAuctionStatus", "ok")
                } else {
                    model.addAttribute("createAuctionStatus", "error")
                }
            }
        }
        model.addAttribute("auctionData", auctionData)
        return "newAuction"
    }

    @GetMapping("/item/image/{id}")
    fun showBrowseItemImage(@PathVariable id: UUID, session: HttpSession, response: HttpServletResponse) {
        val auctions = session.getAttribute("auctions") as List<AuctionBrowseDto>
        if (auctions == null) {
            setupDefaultImage(response)
        } else {
            setupImage(response, auctions, id)
        }
    }

    @GetMapping("/auth/item/image/{id}")
    fun showAccountItemImage(@PathVariable id: UUID, session: HttpSession, response: HttpServletResponse) {
        val account = session.getAttribute("currentUser") as AccountViewDto
        setupImage(response, account.auctions, id)
    }

    private fun setupDefaultImage(response: HttpServletResponse) {
        val str = defaultImgResource.inputStream
        IOUtils.copy(str, response.outputStream)
    }

    private fun setupImage(
        response: HttpServletResponse,
        auctions: List<AuctionBrowseDto>,
        id: UUID
    ) {
        response.contentType = "image/jpeg"
        // val auction = auctions[id]
        val auction = auctions.find {
            it.id == id
        }
        if (auction?.itemImage == null ||
            auction?.itemImage.isEmpty()
        ) {
            setupDefaultImage(response)
        } else {
            val str: InputStream = ByteArrayInputStream(auction?.itemImage)
            IOUtils.copy(str, response.outputStream)
        }
    }

    private fun creditCardFormErrors(bindingResults: BindingResult): Boolean {
        return bindingResults.hasFieldErrors("number") ||
                bindingResults.hasFieldErrors("expirationMonth") ||
                bindingResults.hasFieldErrors("expirationYear") ||
                bindingResults.hasFieldErrors("accountFirstname") ||
                bindingResults.hasFieldErrors("accountLastname") ||
                bindingResults.hasFieldErrors("street") ||
                bindingResults.hasFieldErrors("city") ||
                bindingResults.hasFieldErrors("country") ||
                bindingResults.hasFieldErrors("postalCode")
    }
}
