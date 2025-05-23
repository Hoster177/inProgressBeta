package ru.hoster.inprogress // Убедитесь, что пакет правильный

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ru.hoster.inprogress.navigation.* // Импорт ваших экранов и роутов
import ru.hoster.inprogress.navigation.achievements.AchievementsScreen
import ru.hoster.inprogress.navigation.addeditactivity.AddEditActivityScreen
import ru.hoster.inprogress.navigation.groups.AddEditGroupScreen
import ru.hoster.inprogress.navigation.groups.GroupDetailsScreen
import ru.hoster.inprogress.navigation.groups.GroupsScreen
import ru.hoster.inprogress.ui.theme.InProgressTheme

@AndroidEntryPoint // Важно для Hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InProgressTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() // Вынесли навигацию в отдельную Composable функцию
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.LOGIN) { // Стартовый экран - LOGIN
        composable(Route.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Route.SIGN_UP) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            MainScreen(
                onDailyTimerClick = { navController.navigate(Route.STATISTICS) },
                onAddActivityClick = { navController.navigate(Route.addEditActivity()) },
                onEditGoalClick = { goalId ->
                    navController.navigate(Route.addEditGoal(goalId = goalId))
                },
                onDeleteActivityClick = { /* TODO */ },
                onActivityTimerToggle = { _, _ -> /* TODO */ },
                uiState = TODO(), // Заменить на реальное состояние
                onAddNewGoalClick = {},
                onViewAllGoalsClick = {}
            )
        }
        composable(Screen.Groups.route) {
            GroupsScreen(
                onNavigateToGroupDetails = { groupId -> // Реализуем навигацию
                    navController.navigate(Route.groupDetails(groupId))
                },
                navController = TODO()
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToAchievements = { navController.navigate(Route.ACHIEVEMENTS) },
                onNavigateToHelp = { navController.navigate(Route.HELP_FAQ) },
                onNavigateToUserSettings = { navController.navigate(Route.USER_SETTINGS) },
                navController = navController,
                authViewModel = TODO()
            )
        }

        // --- Вторичные экраны ---
        composable(Route.STATISTICS) {
            StatisticsScreen(navController)
        }
        composable(
            route = Route.addEditActivity(), // Используем helper функцию
            arguments = listOf(navArgument("activityId") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            AddEditActivityScreen(navController = navController)
        }
        composable(
            route = Route.addEditGoal(), // Используем helper функцию
            arguments = listOf(navArgument("goalId") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            AddEditGoalScreen(
                navController = navController,
                goalId = it.arguments?.getString("goalId")
            )
        }
        composable(Route.ACHIEVEMENTS) {
            AchievementsScreen(navController)
        }
        composable(Route.HELP_FAQ) {
            HelpFaqScreen(navController)
        }
        composable(Route.USER_SETTINGS) {
            UserSettingsScreen(navController)
        }

        // --- Экраны, связанные с группами (вторичные) ---
        composable(
            route = Route.addEditGroup(), // Используем helper функцию
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            AddEditGroupScreen(
                navController = navController,
                groupId = backStackEntry.arguments?.getString("groupId")
            )
        }
        composable(
            // Маршрут должен соответствовать определению в Route.kt, включая обязательный аргумент
            route = Route.GROUP_DETAILS + "/{groupId}",
            arguments = listOf(navArgument("groupId") {
                type = NavType.StringType
                // nullable = false // groupId здесь обязателен
            })
        ) {
            GroupDetailsScreen(
                navController = navController,
                viewModel = TODO(),
                groupId = TODO()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InProgressTheme {
        Text("App Preview Holder - Запустите на эмуляторе для проверки навигации")
    }
}