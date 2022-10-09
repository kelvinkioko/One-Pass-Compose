package xml.one.pass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import xml.one.pass.databinding.ActivityMainBinding
import xml.one.pass.domain.preference.OnePassRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    @Inject
    lateinit var onePassRepository: OnePassRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_OnePass)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
        }

        setUpBottomNavController()
    }

    /**
     * TODO (Data store preferences, Biometric enrollment and login)
     * Links
     * https://developer.android.com/guide/navigation/navigation-programmatic
     */
    private fun setUpBottomNavController() {
        binding?.apply {
            bottomNavComponent.bottomNavigationView.background = null

            var isLoggedIn: Boolean

            runBlocking {
                isLoggedIn = onePassRepository.getLoginStatus().getOrNull() ?: false
            }

            val bottomBarBackground =
                bottomNavComponent.bottomAppBar.background as MaterialShapeDrawable
            bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, resources.getDimension(R.dimen.default_corner_radius))
                .build()

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

            navHostFragment.navController.apply {
                if (isLoggedIn) {
                    val navGraph = navInflater.inflate(R.navigation.app_nav_graph)
                    navGraph.setStartDestination(R.id.homeFragment)
                    graph = navGraph
                }
                bottomNavComponent.bottomNavigationView.setupWithNavController(this)
                bottomNavComponent.addPassword.setOnClickListener {
                    this.navigate(R.id.toAddPasswordFragment)
                }
                addOnDestinationChangedListener { _, _, args ->
                    bottomNavComponent.root.isVisible =
                        args?.getBoolean("hasBottomNav", false) == true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
