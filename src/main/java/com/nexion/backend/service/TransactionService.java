package com.nexion.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexion.backend.dto.TransactionRequest;
import com.nexion.backend.dto.TransactionResponse;
import com.nexion.backend.entity.Category;
import com.nexion.backend.entity.Transaction;
import com.nexion.backend.entity.User;
import com.nexion.backend.entity.Wallet;
import com.nexion.backend.repository.CategoryRepository;
import com.nexion.backend.repository.TransactionRepository;
import com.nexion.backend.repository.UserRepository;
import com.nexion.backend.repository.WalletRepository;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository repository, WalletRepository walletRepository,
            CategoryRepository categoryRepository, UserRepository userRepository) {
        this.repository = repository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public TransactionResponse criar(Long walletId, TransactionRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setCreatedBy(createdBy);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        aplicarCategoria(transaction, request.getCategoryId());

        return toResponse(repository.save(transaction));
    }

    public List<TransactionResponse> listarPorCateira(Long walletId) {
        return repository.findByWalletId(walletId).stream().map(this::toResponse).toList();
    }

    public TransactionResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public TransactionResponse atualizar(Long id, TransactionRequest request) {
        Transaction transaction = buscarEntidade(id);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        aplicarCategoria(transaction, request.getCategoryId());
        return toResponse(repository.save(transaction));
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }

    private void aplicarCategoria(Transaction transaction, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }
    }

    private Transaction buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
    }

    private TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = new TransactionResponse();
        response.setId(t.getId());
        response.setWalletId(t.getWallet().getId());
        response.setCategoryId(t.getCategory() != null ? t.getCategory().getId() : null);
        response.setCreatedById(t.getCreatedBy().getId());
        response.setType(t.getType());
        response.setAmount(t.getAmount());
        response.setDescription(t.getDescription());
        response.setDate(t.getDate());
        return response;
    }
}
