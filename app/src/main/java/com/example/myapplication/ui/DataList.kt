package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.MainActivity
import com.example.myapplication.data.Account
import com.example.myapplication.data.MainVM
import com.example.myapplication.encrypt.SecurityEncrypt

@Composable
fun DataList(viewModel: MainVM, navController: NavController, context: MainActivity) {
    viewModel.getData(SecurityEncrypt(context).getData("user_id", 1))
    var account by remember {
        mutableStateOf(
            listOf(
                Account(
                    "Loading...",
                    "Loading...",
                    "Loading...",
                    "Loading...",
                    1,
                    1
                )
            )
        )
    }
    viewModel.accounts.observe(context) {
        account = it
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                content = { Icon(imageVector = Icons.Default.Add, contentDescription = "add") },
                onClick = {
                    navController.popBackStack()
                    navController.navigate("addData")
                }
            )
            FabPosition.End
        }
    ) {
        LazyColumn(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            items(account) { data ->
                columnItem(data, viewModel, navController)
            }
        }
    }

}

@Composable
fun columnItem(data: Account, viewModel: MainVM, navController: NavController) {
    val brush = Brush.horizontalGradient(listOf(Color(0xFFe8b7dd), Color.White))
    val openDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush, RoundedCornerShape(20.dp))
            .clickable {
                val idAccount = data.id
                navController.popBackStack()
                navController.navigate("dataOverview/$idAccount")
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
            ) {
                Text(text = data.site, fontSize = 18.sp)
                Text(text = data.username, fontSize = 12.sp)
            }
            IconButton(onClick = {
                openDialog.value = true
            }) {
                Icon(
                    modifier = Modifier.defaultMinSize(40.dp, 28.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Gray,
                )
            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Do you want to delete it?")},
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        viewModel.deleteData(data)
                        navController.navigate("openList")
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}