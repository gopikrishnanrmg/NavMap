package com.grajeev.navmap

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import io.github.sceneview.SceneView
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Scale
import io.github.sceneview.math.toFloat3
import io.github.sceneview.math.toVector3
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var sceneView: SceneView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var bmpTranslator = BMPTranslator()

        bmpTranslator.setMockOrigin(1355,464)

        var targetCoords = bmpTranslator.translatePointWithMockOrigin(1415, 385)

        var translatedPoints = bmpTranslator.vectorScaleTransFormation(targetCoords.first,targetCoords.second)


        sceneView = findViewById(R.id.arSceneView)
        val camera = sceneView.cameraNode
        val cameraPosition = camera.worldPosition.toVector3()
        val offsetX = translatedPoints.first // 2 meters to the right
        val offsetY = 0.0f // 1 meter up
        val offsetZ = translatedPoints.second
        val offset = Vector3(offsetX, offsetY, offsetZ)
        val newPosition = Vector3.add(cameraPosition,offset)


        val engine = sceneView.engine
        val entityManager = EntityManager.get()
        val lightEntity = entityManager.create()
        LightManager.Builder(LightManager.Type.SUN)
            .color(1.0f, 1.0f, 1.0f)  // White light
            .intensity(100000.0f)            // Intensity of the light
            .direction(0.0f, -1.0f, 0.0f)    // Direction pointing downwards
            .build(engine, lightEntity)
        sceneView.scene.addEntity(lightEntity)


        lifecycleScope.launch {
            val modelLoader = ModelLoader(sceneView.engine, this@MainActivity)
            val uri = Uri.parse("android.resource://${packageName}/${R.raw.cubone}")
            val modelInstance = modelLoader.loadModel(uri.toString())
            val modelNode = ModelNode(modelInstance!!.instance)
            modelNode.worldPosition = newPosition.toFloat3()
            sceneView.addChildNode(modelNode)
//            sceneView.onGestureListener {
//                updateObjectPosition(camera.worldPosition.toVector3(), modelNode)
//            }
            while(true){
                updateObjectScale(sceneView.cameraNode.worldPosition.toVector3(), modelNode)
                delay(16)
            }
        }
    }


    private fun updateObjectScale(cameraPosition: Vector3, modelNode: ModelNode) {
        val objectPosition = modelNode.worldPosition.toVector3()
        val distance = Vector3.subtract(cameraPosition, objectPosition).length()
        if (distance > 10f) {
//            val newPosition = Vector3.add(objectPosition, Vector3(0.0f, 0.0f, 0.1f))
//            modelNode.worldPosition = newPosition.toFloat3()
            modelNode.scale = Scale(1.0f*distance/10,1.0f*distance/10,1.0f*distance/10)
        }
    }

}
