package com.nestly.backend.support

class BusinessRuleException(message: String) : RuntimeException(message)

class LockedAccountException(message: String) : RuntimeException(message)

class NotFoundException(message: String) : RuntimeException(message)
