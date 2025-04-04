package com.example.socialcomposeapp.screens.publicprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatsRow(postCount: Int, followerCount: Int, followingCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(count = postCount, label = "Posts")
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = Color.Gray.copy(alpha = 0.5f)
        )
        StatItem(count = followerCount, label = "Followers")
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = Color.Gray.copy(alpha = 0.5f)
        )
        StatItem(count = followingCount, label = "Following")
    }
}

@Composable
fun StatItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xffE88AE8)
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}