package com.nexion.backend.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nexion.backend.dto.MonthlyReportResponse;
import com.nexion.backend.dto.WalletSummaryResponse;
import com.nexion.backend.entity.Transaction;
import com.nexion.backend.enums.TransactionType;
import com.nexion.backend.repository.TransactionRepository;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final WalletAccessService walletAccessService;

    public DashboardService(TransactionRepository transactionRepository, WalletAccessService walletAccessService) {
        this.transactionRepository = transactionRepository;
        this.walletAccessService = walletAccessService;
    }

    public WalletSummaryResponse resumo(Long walletId) {
        walletAccessService.verificarMembro(walletId);
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        BigDecimal totalIncome = somarPorTipo(transactions, TransactionType.INCOME);
        BigDecimal totalExpense = somarPorTipo(transactions, TransactionType.EXPENSE);

        WalletSummaryResponse response = new WalletSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setBalance(totalIncome.subtract(totalExpense));
        response.setTransactionCount((long) transactions.size());
        return response;
    }

    private BigDecimal somarPorTipo(List<Transaction> transactions, TransactionType type) {
        return transactions.stream().filter(t -> t.getType() == type).map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public List<MonthlyReportResponse> relatorioMensal(Long walletId) {
        walletAccessService.verificarMembro(walletId);

        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        /*O groupinBy faz a lista do mês */
        Map<YearMonth, List<Transaction>> porMes = transactions.stream().collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate())));
        /*O YearMonth trava só o mês e o ano */
        return porMes.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> {
            /*O sorted ordena por mês e ano */
            MonthlyReportResponse r = new MonthlyReportResponse();
            r.setMonth(entry.getKey().toString());
            r.setIncome(somarPorTipo(entry.getValue(), TransactionType.INCOME));
            r.setExpense(somarPorTipo(entry.getValue(), TransactionType.EXPENSE));
            return r;
        })
        .toList();
    }
}