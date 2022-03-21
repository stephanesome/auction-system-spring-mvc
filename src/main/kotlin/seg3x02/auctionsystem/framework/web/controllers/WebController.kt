package seg3x02.auctionsystem.framework.web.controllers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.framework.web.forms.AccountForm
import seg3x02.auctionsystem.framework.web.forms.AuctionForm
import seg3x02.auctionsystem.framework.web.forms.BidForm
import seg3x02.auctionsystem.framework.web.forms.SearchRequest
import seg3x02.auctionsystem.framework.web.services.AuctionService
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.math.BigDecimal
import java.security.Principal
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import kotlin.collections.ArrayList

@Controller
class WebController(private val auctionService: AuctionService) {
    @RequestMapping("/")
    fun showWelcome(model: Model, session: HttpSession): String {
        val searchRequest = SearchRequest()
        model.addAttribute("searchRequest", searchRequest)
        session.setAttribute("searchData", searchRequest)
        return "welcome"
    }

    @GetMapping("/login")
    fun login(model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        return "login"
    }

    @PostMapping(value = ["/search"])
    fun auctionSearch(searchRequest: SearchRequest, model: Model, session: HttpSession): String {
        // get list of auctions with service
        session.setAttribute("searchData", searchRequest)
        val auctions = auctionService.findAuctions(searchRequest.category)
        session.setAttribute("auctions", auctions)
        model.addAttribute("auctions", auctions)
        return "browseAuctions"
    }

    @GetMapping(value = ["/search"])
    fun showSearchResult(model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        val auctions = session.getAttribute("auctions")?: ArrayList<AuctionBrowseDto>()
        model.addAttribute("auctions", auctions)
        return "browseAuctions"
    }

    @GetMapping("/auction")
    fun showAuction(@RequestParam selectedAuction: Int, model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        val auction = (session.getAttribute("auctions") as List<AuctionBrowseDto>)[selectedAuction]
        model.addAttribute("auction", auction)
        model.addAttribute("bidForm", BidForm())
        model.addAttribute("bidResult","")
        session.setAttribute("selected", auction)
        return "auction"
    }

    @PostMapping(value = ["/auth/placeBid"])
    fun placeBid(bidForm: BidForm,  model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        val auction = session.getAttribute("selected") as AuctionBrowseDto
        model.addAttribute("auction", auction)
        if (auction.currentMinBid > BigDecimal(bidForm.amount)) {
            model.addAttribute("bidResult","lowAmount")
        } else {
            val account = session.getAttribute("currentUser") as AccountViewDto
            if (account.userName == auction.sellerId) {
                model.addAttribute("bidResult","seller-bid")
            } else {
                // call placebid with current user
                if (auctionService.placeBid(account.userName, auction.id, bidForm.amount)) {
                    model.addAttribute("bidResult", "ok")
                } else {
                    model.addAttribute("bidResult", "error")
                }
            }
        }
        return "auction"
    }

    @GetMapping(value = ["/register"])
    fun register(model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        val accountData = AccountForm()
        model.addAttribute("accountData", accountData)
        return "createAccount"
    }

    @PostMapping(value = ["/register"])
    fun createAccount(accountData: AccountForm, model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        if (auctionService.createAccount(accountData)) {
            model.addAttribute("createAccountStatus", "ok")
            model.addAttribute("accountData", AccountForm())
        } else {
            model.addAttribute("createAccountStatus", "error")
            model.addAttribute("accountData", accountData)
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
        val searchData = session.getAttribute("searchData") ?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        var account = session.getAttribute("currentUser") as AccountViewDto
        val accountData = if (account != null) {
            auctionService.setAccountForm(account)
        } else {
            AccountForm()
        }
        model.addAttribute("accountData", accountData)
        return "updateAccount"
    }

    @PostMapping(value = ["/auth/updateAccount"])
    fun doUpdateAccount(accountData: AccountForm, model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        // call service to update - pass account and accountData
        var account = session.getAttribute("currentUser") as AccountViewDto
        auctionService.updateAccount(account, accountData)
        return "updateAccount"
    }

    private fun setAccountViewModel(
        session: HttpSession,
        model: Model,
        principal: Principal
    ) {
        val searchData = session.getAttribute("searchData") ?: SearchRequest()
        model.addAttribute("searchRequest", searchData)
        var account = session.getAttribute("currentUser")
        if (account == null) {
            val userid = principal.name
            account = auctionService.getAccount(userid)
            session.setAttribute("currentUser", account)
        }
        model.addAttribute("account", account)
    }

    @GetMapping(value = ["/auth/deactivateAccount"])
    fun deactivateAccount() {

    }

    @GetMapping(value = ["/auth/newAuction"])
    fun newAuction(model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")
        model.addAttribute("searchRequest", searchData)
        model.addAttribute("createAuctionStatus", null)
        val auctionData = AuctionForm()
        model.addAttribute("auctionData", auctionData)
        return "newAuction"
    }

    @PostMapping(value = ["/auth/createAuction"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addAuction(auctionData: AuctionForm, model: Model, session: HttpSession): String {
        val searchData = session.getAttribute("searchData")
        model.addAttribute("searchRequest", searchData)
        // create the auction here
        val account = session.getAttribute("currentUser") as AccountViewDto
        if (auctionService.createAuction(account, auctionData)) {
            model.addAttribute("createAuctionStatus", "ok")
        } else {
            model.addAttribute("createAuctionStatus", "error")
        }

        model.addAttribute("auctionData", auctionData)
        return "newAuction"
    }

    @GetMapping("/item/image/{id}")
    fun showBrowseItemImage(@PathVariable id: UUID, session: HttpSession, response: HttpServletResponse) {
        val auctions = session.getAttribute("auctions") as List<AuctionBrowseDto>
        setupImage(response, auctions, id)
    }

/*    @GetMapping("/auth/selectedItem/image")
    fun showSelectedItemImage(session: HttpSession, response: HttpServletResponse) {
        val auction = session.getAttribute("selected") as AuctionBrowseDto
        response.contentType = "image/jpeg"
        val str: InputStream = ByteArrayInputStream(auction.itemImage)
        IOUtils.copy(str, response.outputStream)
    }*/

    @GetMapping("/auth/item/image/{id}")
    fun showAccountItemImage(@PathVariable id: UUID, session: HttpSession, response: HttpServletResponse) {
        val account = session.getAttribute("currentUser") as AccountViewDto
        setupImage(response, account.auctions, id)
    }
    private fun setupImage(
        response: HttpServletResponse,
        auctions: List<AuctionBrowseDto>,
        id: UUID) {
        response.contentType = "image/jpeg"
        // val auction = auctions[id]
        val auction = auctions.find {
            it.id == id
        }
        val str: InputStream = ByteArrayInputStream(auction?.itemImage)
        IOUtils.copy(str, response.outputStream)
    }
}
