package com.mz.identity.service;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.auth.AuthRequest;
import com.mz.identity.auth.AuthStrategy;
import com.mz.identity.enums.AuthType;
import com.mz.identity.exception.UnsupportedLoginMethodException;
import com.mz.identity.mappers.AccountMapper;
import com.mz.identity.mappers.RoleMapper;
import com.mz.identity.models.Account;
import com.mz.identity.repository.AccountRepository;
import com.mz.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final List<AuthStrategy> strategies;
    private final JwtService jwtService;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public String login(AuthRequest request) {
        AuthStrategy strategy = strategies.stream()
                .filter(s->s.supports(request.getType()))
                .findFirst()
                .orElseThrow(()->new UnsupportedLoginMethodException("Unsupported Login Method Exception"));

        UserPrincipal principal = strategy.authenticate(request);

        Account account = accountRepository.findByEmail(principal.getEmail())
                .map(existingAccount -> updateExistingAccount(existingAccount, request, principal))
                .orElseGet(() -> createNewAccount(principal, request.getType()));

        Account savedAccount =accountRepository.save(account);

        return jwtService.generateToken(accountMapper.toUserPrincipal(savedAccount));
    }

    private Account createNewAccount(UserPrincipal principal, AuthType type){
        Account account = Account.builder()
                .email(principal.getEmail())
                .roles(principal
                        .getRoles()
                        .stream().map(roleMapper::mapRoleFromCommonToModel)
                        .collect(Collectors.toSet())
                ).build();

        switch (type){
            case GOOGLE -> account.setGoogleId(principal.getUserId());
        }

        return account;
    }

    private Account updateExistingAccount(Account existingAccount, AuthRequest request, UserPrincipal principal) {
        switch (request.getType()) {
            case GOOGLE -> {
                if (existingAccount.getGoogleId() == null) {
                    existingAccount.setGoogleId(principal.getUserId());
                }
            }
        }

        return existingAccount;
    }
}
