package com.example.splitit.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitit.viewmodel.BillViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: BillViewModel = viewModel()
    var scrollToTop by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = "bill_list") {
        composable("bill_list") {
            BillListScreen(
                viewModel = viewModel,
                onAddBill = { navController.navigate("add_bill") },
                onBillClick = { billId -> navController.navigate("bill_detail/$billId") },
                scrollToTop = scrollToTop,
                onScrollToTopDone = { scrollToTop = false }
            )
        }
        composable("add_bill") {
            AddBillScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    scrollToTop = true
                    navController.popBackStack()
                }
            )
        }
        composable("bill_detail/{billId}") { backStackEntry ->
            val billId = backStackEntry.arguments?.getString("billId")?.toLongOrNull()
            val bill = viewModel.bills.value.find { it.id == billId }
            if (bill != null) {
                BillDetailScreen(
                    bill = bill,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onEditBill = { navController.navigate("edit_bill/${bill.id}") }
                )
            }
        }
        composable("edit_bill/{billId}") { backStackEntry ->
            val billId = backStackEntry.arguments?.getString("billId")?.toLongOrNull()
            val bill = viewModel.bills.value.find { it.id == billId }
            if (bill != null) {
                EditBillScreen(
                    bill = bill,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}