package com.nnk.springboot.config;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.UserForm;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Initialise la base de données H2 avec des données de démonstration.
 * Actif uniquement avec le profil "demo".
 */
@Component
@Profile("demo")
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final BidListService bidListService;
    private final CurvePointService curvePointService;
    private final RatingService ratingService;
    private final RuleNameService ruleNameService;
    private final TradeService tradeService;

    public DataInitializer(UserService userService, BidListService bidListService,
                           CurvePointService curvePointService, RatingService ratingService,
                           RuleNameService ruleNameService, TradeService tradeService) {
        this.userService = userService;
        this.bidListService = bidListService;
        this.curvePointService = curvePointService;
        this.ratingService = ratingService;
        this.ruleNameService = ruleNameService;
        this.tradeService = tradeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadUsers();
        loadBidLists();
        loadCurvePoints();
        loadRatings();
        loadRuleNames();
        loadTrades();
    }

    private void loadUsers() {
        userService.save(new UserForm(null, "admin", "Admin123!", "Administrator", "ADMIN"));
        userService.save(new UserForm(null, "user",  "User1234!", "Standard User",  "USER"));
    }

    private void loadBidLists() {
        bidListService.save(new BidList("Goldman Sachs",    "BOND",   100.0));
        bidListService.save(new BidList("JP Morgan",        "EQUITY", 250.5));
        bidListService.save(new BidList("Morgan Stanley",   "BOND",   175.0));
        bidListService.save(new BidList("Citigroup",        "EQUITY", 320.0));
        bidListService.save(new BidList("Bank of America",  "BOND",   210.0));
        bidListService.save(new BidList("Deutsche Bank",    "EQUITY", 140.0));
        bidListService.save(new BidList("Barclays",         "BOND",   195.0));
        bidListService.save(new BidList("HSBC",             "EQUITY", 280.0));
        bidListService.save(new BidList("BNP Paribas",      "BOND",   165.0));
        bidListService.save(new BidList("Société Générale", "EQUITY", 230.0));
        bidListService.save(new BidList("Credit Suisse",    "BOND",   185.0));
        bidListService.save(new BidList("UBS",              "EQUITY", 310.0));
        bidListService.save(new BidList("Nomura",           "BOND",   120.0));
        bidListService.save(new BidList("Santander",        "EQUITY", 270.0));
        bidListService.save(new BidList("ING Group",        "BOND",   155.0));
    }

    private void loadCurvePoints() {
        curvePointService.save(new CurvePoint(1, 0.25, 1.85));
        curvePointService.save(new CurvePoint(1, 0.50, 2.10));
        curvePointService.save(new CurvePoint(1, 1.00, 2.45));
        curvePointService.save(new CurvePoint(1, 2.00, 2.80));
        curvePointService.save(new CurvePoint(1, 5.00, 3.20));
        curvePointService.save(new CurvePoint(2, 0.25, 1.90));
        curvePointService.save(new CurvePoint(2, 0.50, 2.15));
        curvePointService.save(new CurvePoint(2, 1.00, 2.50));
        curvePointService.save(new CurvePoint(2, 2.00, 2.95));
        curvePointService.save(new CurvePoint(2, 5.00, 3.40));
        curvePointService.save(new CurvePoint(3, 0.25, 1.95));
        curvePointService.save(new CurvePoint(3, 0.50, 2.20));
        curvePointService.save(new CurvePoint(3, 1.00, 2.60));
        curvePointService.save(new CurvePoint(3, 2.00, 3.05));
        curvePointService.save(new CurvePoint(3, 5.00, 3.55));
    }

    private void loadRatings() {
        ratingService.save(new Rating("Aaa",  "AAA",  "AAA",  1));
        ratingService.save(new Rating("Aa1",  "AA+",  "AA+",  2));
        ratingService.save(new Rating("Aa2",  "AA",   "AA",   3));
        ratingService.save(new Rating("Aa3",  "AA-",  "AA-",  4));
        ratingService.save(new Rating("A1",   "A+",   "A+",   5));
        ratingService.save(new Rating("A2",   "A",    "A",    6));
        ratingService.save(new Rating("A3",   "A-",   "A-",   7));
        ratingService.save(new Rating("Baa1", "BBB+", "BBB+", 8));
        ratingService.save(new Rating("Baa2", "BBB",  "BBB",  9));
        ratingService.save(new Rating("Baa3", "BBB-", "BBB-", 10));
        ratingService.save(new Rating("Ba1",  "BB+",  "BB+",  11));
        ratingService.save(new Rating("Ba2",  "BB",   "BB",   12));
        ratingService.save(new Rating("Ba3",  "BB-",  "BB-",  13));
        ratingService.save(new Rating("B1",   "B+",   "B+",   14));
        ratingService.save(new Rating("B2",   "B",    "B",    15));
    }

    private void loadRuleNames() {
        ruleNameService.save(new RuleName("Risk Limit",       "Maximum exposure per counterparty",  "{\"limit\": 1000000}",  "risk_limit.tpl",   "SELECT * FROM trades WHERE exposure > ?",     "exposure > limit"));
        ruleNameService.save(new RuleName("Margin Call",      "Trigger margin call when threshold", "{\"threshold\": 0.8}",  "margin_call.tpl",  "SELECT * FROM trades WHERE margin < ?",       "margin < threshold"));
        ruleNameService.save(new RuleName("Stop Loss",        "Automatic stop loss rule",           "{\"stop\": 0.05}",      "stop_loss.tpl",    "SELECT * FROM trades WHERE loss > ?",         "loss > stop_pct"));
        ruleNameService.save(new RuleName("Concentration",    "Max concentration per asset class",  "{\"max_pct\": 0.25}",   "concentration.tpl","SELECT * FROM bids WHERE pct > ?",            "pct > max_pct"));
        ruleNameService.save(new RuleName("Liquidity",        "Minimum liquidity coverage",         "{\"min_lcr\": 1.0}",    "liquidity.tpl",    "SELECT * FROM assets WHERE lcr < ?",          "lcr < min_lcr"));
        ruleNameService.save(new RuleName("Credit Spread",    "Alert on credit spread widening",    "{\"spread\": 200}",     "credit.tpl",       "SELECT * FROM ratings WHERE spread > ?",      "spread > threshold"));
        ruleNameService.save(new RuleName("VaR Limit",        "Value at Risk daily limit",          "{\"var\": 500000}",     "var_limit.tpl",    "SELECT * FROM trades WHERE var > ?",          "var > daily_limit"));
        ruleNameService.save(new RuleName("Duration",         "Portfolio duration constraint",      "{\"max_dur\": 7.0}",    "duration.tpl",     "SELECT * FROM bonds WHERE duration > ?",      "duration > max_dur"));
        ruleNameService.save(new RuleName("Counterparty",     "Counterparty exposure rule",         "{\"max_exp\": 5}",      "counterparty.tpl", "SELECT * FROM trades WHERE cpty_count > ?",   "count > max_exp"));
        ruleNameService.save(new RuleName("Sector Exposure",  "Max exposure per sector",            "{\"sector_max\": 0.3}", "sector.tpl",       "SELECT * FROM trades WHERE sector_pct > ?",  "sector_pct > max"));
        ruleNameService.save(new RuleName("Leverage Ratio",   "Maximum leverage constraint",        "{\"max_lev\": 3.0}",    "leverage.tpl",     "SELECT * FROM trades WHERE leverage > ?",     "leverage > max_lev"));
        ruleNameService.save(new RuleName("FX Exposure",      "Foreign exchange exposure limit",    "{\"max_fx\": 0.1}",     "fx_exposure.tpl",  "SELECT * FROM trades WHERE fx_pct > ?",       "fx_pct > max_fx"));
        ruleNameService.save(new RuleName("Drawdown",         "Maximum drawdown alert",             "{\"max_dd\": 0.15}",    "drawdown.tpl",     "SELECT * FROM portfolios WHERE dd > ?",       "dd > max_dd"));
        ruleNameService.save(new RuleName("Volatility",       "Portfolio volatility ceiling",       "{\"max_vol\": 0.20}",   "volatility.tpl",   "SELECT * FROM portfolios WHERE vol > ?",      "vol > max_vol"));
        ruleNameService.save(new RuleName("Correlation",      "Asset correlation threshold",        "{\"max_corr\": 0.8}",   "correlation.tpl",  "SELECT * FROM assets WHERE corr > ?",         "corr > max_corr"));
    }

    private void loadTrades() {
        tradeService.save(new Trade("Goldman Sachs",    "BUY",  1000.0));
        tradeService.save(new Trade("JP Morgan",        "SELL",  500.0));
        tradeService.save(new Trade("Morgan Stanley",   "BUY",  1500.0));
        tradeService.save(new Trade("Citigroup",        "SELL",  750.0));
        tradeService.save(new Trade("Bank of America",  "BUY",  2000.0));
        tradeService.save(new Trade("Deutsche Bank",    "SELL", 1200.0));
        tradeService.save(new Trade("Barclays",         "BUY",   900.0));
        tradeService.save(new Trade("HSBC",             "SELL", 1100.0));
        tradeService.save(new Trade("BNP Paribas",      "BUY",  1300.0));
        tradeService.save(new Trade("Société Générale", "SELL",  850.0));
        tradeService.save(new Trade("Credit Suisse",    "BUY",  1750.0));
        tradeService.save(new Trade("UBS",              "SELL",  620.0));
        tradeService.save(new Trade("Nomura",           "BUY",   980.0));
        tradeService.save(new Trade("Santander",        "SELL", 1450.0));
        tradeService.save(new Trade("ING Group",        "BUY",  1150.0));
    }
}