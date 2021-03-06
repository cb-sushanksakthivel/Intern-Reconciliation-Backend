package com.cb.reconciliation;

import com.cb.reconciliation.model.*;
import com.cb.reconciliation.model.credentials.*;
import com.cb.reconciliation.service.ChargebeeConnect;
import com.cb.reconciliation.service.MismatchedTransactions;
import com.cb.reconciliation.service.StripeConnect;
import com.cb.reconciliation.service.XeroConnect;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ReconciliationApplicationTests {
    String xeroTenantId = "e39e9d62-44ae-4814-adbc-3b897c9b67bd";
    String refreshToken = "";
    String clientId = "";
    String clientSecret = "";
    String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE2NDg3MDcwNjQsImV4cCI6MTY0ODcwODg2NCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiMjhCMkNBMjc5OTczNDNCQUI4OTg0MkQ5NENCRkVGNDIiLCJzdWIiOiIwMDEyMWJiMmIxYTQ1MmJmYjIzODk3MzE5MjYzODU1ZSIsImF1dGhfdGltZSI6MTY0ODcwNjkzNywieGVyb191c2VyaWQiOiJlYWEzNmM1Yi1jZmI1LTQ1NDQtOGY4Mi0wZWE5ODBiYjY3ZWMiLCJnbG9iYWxfc2Vzc2lvbl9pZCI6IjFmOTM3MWU0MTQ4ZDRlZGY4Y2JhZDZiZGExMmRlMTlhIiwianRpIjoiYWI5MjdjYThjOGRiMTA1Yjg5ZDNmZDg3ZjNiMTU4YTUiLCJhdXRoZW50aWNhdGlvbl9ldmVudF9pZCI6IjY5NWQxMWNmLTgyNzUtNGE3Zi05OGZiLTJiZDliYjk4M2M4NSIsInNjb3BlIjpbImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.yHGHnN6mZm5rj1s9KsC4TB7gbUITWMXZEkaIEh5oplUsu7f7sMjV3pS-mgIa0SsFvPE9h3PFJYYXMUGVx_bnK5BZbVFGM-luEdsgPBcHy8T_gPtu7YnvKeoLbWuxXsUHGNB7puq27fyUlbMzaPUBZMiZUSirzX1W_m4dYXB0NFwPFSmF3sZKG8nWaYtIQIjb92xiE-G0sra6ICpnLddigteT-SPOWXzjMS0ZjAD6IdR0zX98NXNR59lwguN7hGP2LqhrlxQiK-hVhdvZGlvefTegaw16LSZF27f6SNdVNdb7HO0fWeTiZCQBLXTq76QJ5qIFrO1jEFAKFC3zvpXARw";
    // XeroCredentials cred = new XeroCredentials(clientId, clientSecret, refreshToken, xeroTenantId);
    XeroCredentials xeroCredentials = new XeroCredentials(xeroTenantId, accessToken);

    String chargebeeSiteUrl = "reconciletest-test";
    String chargebeeApiKey = "test_rBsnVbkoMt0ecuqSQlRfH1xYqe3qXBqrJ";
    ChargebeeCredentials chargebeeCredentials = new ChargebeeCredentials(chargebeeSiteUrl, chargebeeApiKey);

    String stripeApiKey = "sk_test_51KgIfiSFiiJc1ZKRsk9hPULL1qJ1ZQf22YFf5CmXSQLAgDarsH2vSyfUT9g6Hdaunow7kuAzyy6tA3Lxi7psnoNo00J18f0HDc";
    StripeCredentials stripeCredentials = new StripeCredentials(stripeApiKey);

    LocalDateTime startDate = LocalDateTime.of(2022, 3, 22, 0, 0);
    LocalDateTime endDate = LocalDateTime.now();

    Timestamp startTime = Timestamp.valueOf(startDate);
    Timestamp endTime = Timestamp.valueOf(endDate);

    @Test void testXero(){
        XeroConnect conn = new XeroConnect();
        List<Transaction> transactions = conn.getTranscations(xeroCredentials, startTime, endTime);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testChargebee() throws Exception {
        ChargebeeConnect conn = new ChargebeeConnect();
        List<Transaction> transactions;
//        com.chargebee.models.Transaction.Type transactionType = com.chargebee.models.Transaction.Type.REFUND;

        transactions = conn.getTransactionsByGateway(chargebeeCredentials, GatewayEnum.STRIPE, startTime, endTime);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

//    @Test
    void testStripe() throws StripeException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getTransactions(stripeCredentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

//    @Test
    void testStripeRefund() throws StripeException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getRefunds(stripeCredentials, startTimestamp, endTimestamp);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void testStripeBalanceTransaction() throws StripeException {
        StripeConnect conn = new StripeConnect();
        List<Transaction> transactions;

        transactions = conn.getBalanceTransaction(stripeCredentials, startTime, endTime);
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }

    @Test
    void mismatched() throws Exception {
        MismatchedTransactions computer = new MismatchedTransactions();

        Map<GatewayEnum, GatewayCredentials> gatewayCredentialsMap = new HashMap<>();
        gatewayCredentialsMap.put(GatewayEnum.STRIPE, stripeCredentials);

        Map<AccSoftEnum, AccSoftCredentials> accSoftCredentialsMap = new HashMap<>();
        accSoftCredentialsMap.put(AccSoftEnum.XERO, xeroCredentials);

        computer.mismatched(chargebeeCredentials, gatewayCredentialsMap, accSoftCredentialsMap, startTime, endTime);
    }
}
