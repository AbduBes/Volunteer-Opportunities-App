package com.example.volunteer.model

import com.example.volunteer.domain.data.Quest

object HardcodedData {
    val allQuests = listOf(
        Quest(1, "Park Cleanup", "Green City", "Environment", "City Park", 3, "Help clean the local park and plant new flowers."),
        Quest(2, "Food Bank Helper", "Community Aid", "Food", "Downtown", 4, "Sort and distribute food donations to families in need.", isJoined = true),
        Quest(3, "Animal Shelter", "Paws & Care", "Animals", "West Side", 2, "Walk and care for shelter animals awaiting adoption.", isJoined = true),
        Quest(4, "Reading Buddy", "City Library", "Education", "Main Library", 2, "Read books with children aged 5–10 every Saturday."),
        Quest(5, "Senior Visit", "Elder Care", "Healthcare", "Sunrise Home", 3, "Visit and chat with elderly residents."),
        Quest(6, "Beach Cleanup", "Ocean Friends", "Environment", "South Beach", 5, "Remove plastic waste from the coastline."),
        Quest(7, "Coding Workshop", "Tech4Good", "Education", "Community Center", 4, "Teach basic coding to teenagers."),
    )

    val categories = listOf("All", "Environment", "Food", "Animals", "Education", "Healthcare")
}