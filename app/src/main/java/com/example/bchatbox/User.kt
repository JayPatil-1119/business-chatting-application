package com.example.bchatbox

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var hasUnreadMessages: Boolean = false // Add this line

    constructor() {}

    constructor(name: String?, email: String?, uid: String?, hasUnreadMessages: Boolean = false) {
        this.name = name
        this.email = email
        this.uid = uid
        this.hasUnreadMessages = hasUnreadMessages // Initialize this property
    }
}