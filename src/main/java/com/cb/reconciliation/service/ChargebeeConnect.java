package com.cb.reconciliation.service;

import com.cb.reconciliation.model.credentials.ChargebeeCredentials;
import com.cb.reconciliation.model.GatewayEnum;
import com.cb.reconciliation.model.Transaction;
import com.chargebee.Environment;
import com.chargebee.ListResult;
import com.chargebee.filters.enums.SortOrder;
import com.chargebee.models.Customer;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChargebeeConnect {

    public List<Transaction> getTransactionsByGateway(
            ChargebeeCredentials credentials,
            GatewayEnum gatewayEnumType,
            Timestamp startDate,
            Timestamp endDate
//            com.chargebee.models.Transaction.Type transactionType
            ) throws Exception {
        // Choose Gateway
        com.chargebee.models.enums.Gateway gatewayEnumVal;
        switch (gatewayEnumType) {
            case STRIPE:
                gatewayEnumVal = com.chargebee.models.enums.Gateway.STRIPE;
                break;
            default:
                gatewayEnumVal = com.chargebee.models.enums.Gateway.CHARGEBEE;
        }

        // Chargebee API
        Environment.configure(credentials.getSiteName(), credentials.getAPIKey());
        ListResult result = com.chargebee.models.Transaction.list()
//                .limit(10).offset(10)
                .date().between(startDate, endDate)
                .gateway().is(gatewayEnumVal)
                .sortByDate(SortOrder.DESC)
//                .type().is(transactionType)
                .request();

        List<Transaction> transactions = new ArrayList<>();
        for(ListResult.Entry entry:result) {
            com.chargebee.models.Transaction cb_transaction = entry.transaction();
            Environment.configure(credentials.getSiteName(), credentials.getAPIKey());
            ListResult res= com.chargebee.models.Customer.list().id().is(cb_transaction.customerId()).request();
            String customer_name="";
            for(ListResult.Entry entry2:res){
                com.chargebee.models.Customer cb_customer = entry2.customer();
                if(cb_customer.lastName()!=null) {
                    customer_name=cb_customer.firstName()+" "+cb_customer.lastName();
                }
                else{
                    customer_name=cb_customer.firstName();
                }
            }
            String idAtGateway = cb_transaction.idAtGateway();
            double amount = cb_transaction.amount();
            String currencyCode = cb_transaction.currencyCode();
            String paymentMethod = cb_transaction.paymentMethod().toString();
            String gateWay = cb_transaction.gateway().toString();
            LocalDateTime date = cb_transaction.date().toLocalDateTime();

            com.chargebee.models.Transaction.Type type = cb_transaction.type();
            String transactionType = null;
            if (type == com.chargebee.models.Transaction.Type.PAYMENT) {
                transactionType = "payment";
            } else if (type == com.chargebee.models.Transaction.Type.REFUND) {
                transactionType = "refund";
            }

            Transaction tr = new Transaction(idAtGateway, customer_name, date, amount, currencyCode, transactionType, paymentMethod, gateWay);
            transactions.add(tr);
        }
//        System.out.println("CB");
//        System.out.println(transactions);
        return transactions;
    }
}
