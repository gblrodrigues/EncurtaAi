package com.gblrod.encurtaai.exception

class ShortCodeAlreadyExistsException(code: String) : RuntimeException("Short code '$code' already exists.")