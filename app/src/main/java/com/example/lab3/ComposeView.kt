package com.example.lab3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun MyComposeView() {
    MaterialTheme {
        ShowBox()
    }
}
@Composable
fun ShowBox(){
    Box(modifier = Modifier.size(200.dp)){
        Surface (color = Color.Green,
            modifier = Modifier
                .size(150.dp)
                .align(alignment = Alignment.Center)
        ){ }
//        Icon(
//            Icons.Filled.CheckCircle,
//            contentDescription = null,
//            tint = Color.Yellow,
//            modifier = Modifier
//                .align(alignment = Alignment.Center)
//                .size(120.dp)
//        )
        Surface (color = Color.Black,
            modifier = Modifier
                .size(120.dp)
                .align(alignment = Alignment.Center)
        ){ }
        Surface (color = Color.Blue,
            modifier = Modifier
                .size(90.dp)
                .align(alignment = Alignment.Center)
        ){ }
    }
}