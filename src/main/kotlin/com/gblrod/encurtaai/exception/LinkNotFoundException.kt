package com.gblrod.encurtaai.exception

class LinkNotFoundException(code: String) : RuntimeException("Link with code '$code' was not found.")