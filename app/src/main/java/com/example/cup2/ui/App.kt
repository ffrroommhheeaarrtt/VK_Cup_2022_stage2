package com.example.cup2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cup2.ui.screen.article_rating.ArticleRatingScreen
import com.example.cup2.ui.screen.drag_and_drop.DragAndDropScreen
import com.example.cup2.ui.screen.element_matching.ElementMatchingScreen
import com.example.cup2.ui.screen.filling_in_gaps.FillingInGapsScreen
import com.example.cup2.ui.screen.main.MainScreen
import com.example.cup2.ui.screen.quiz.QuizScreen
import com.example.cup2.ui.theme.Cup2Theme

@Composable
fun App() {
    Cup2Theme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = NAVIGATION_MENU_MAIN,
        ) {
            composable(route = NAVIGATION_MENU_MAIN) {
                MainScreen(onMenuButtonClick = { navController.navigate(it) })
            }
            NavigationMenu.values().forEach { menu ->
                composable(route = menu.name) {
                    when (menu) {
                        NavigationMenu.QUIZ -> QuizScreen()
                        NavigationMenu.ELEMENT_MATCHING -> ElementMatchingScreen()
                        NavigationMenu.DRAG_AND_DROP -> DragAndDropScreen()
                        NavigationMenu.FILLING_IN_GAPS -> FillingInGapsScreen()
                        NavigationMenu.ARTICLE_RATING -> ArticleRatingScreen()
                    }
                }
            }
        }
    }
}