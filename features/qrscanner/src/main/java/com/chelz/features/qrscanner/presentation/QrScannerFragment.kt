package com.chelz.features.qrscanner.presentation

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.chelz.features.qrscanner.databinding.FragmentQrScannerBinding
import com.chelz.features.qrscanner.domain.QrCodeAnalyzer
import com.chelz.features.qrscanner.domain.ScanningResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrScannerFragment : Fragment() {

	companion object {

		private const val cameraPermissionRequestCode = 1
		private val REQUIRED_PERMISSIONS = arrayOf(
			Manifest.permission.CAMERA,
		)
	}

	private val activityResultLauncher = registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { permissions ->
		var permissionGranted = true
		permissions.entries.forEach {
			if (it.key in REQUIRED_PERMISSIONS && !it.value)
				permissionGranted = false
		}
		if (!permissionGranted) {
			Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
		} else {
			startCamera()
		}
	}

	private var _binding: FragmentQrScannerBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<QrScannerViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentQrScannerBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		activityResultLauncher.launch(REQUIRED_PERMISSIONS)
	}

	private fun startCamera() {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

		val listener = object : ScanningResultListener {
			override fun onScanned(result: String) {
				requireActivity().runOnUiThread {
					viewModel.onQrScanned(result)
					this@QrScannerFragment.stopCamera()
					return@runOnUiThread
				}
			}
		}
		cameraProviderFuture.addListener(
			{
				val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
				val preview = Preview.Builder()
					.build()
					.also {
						it.setSurfaceProvider(binding.cameraPreview.createSurfaceProvider())
					}
				val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

				val analyser = QrCodeAnalyzer(listener)
				try {
					val imageAnalysis = ImageAnalysis.Builder()
						.setTargetResolution(Size(1280, 720))
						.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
						.build()

					imageAnalysis.setAnalyzer(Dispatchers.Main.asExecutor(), analyser)
					cameraProvider.unbindAll()
					cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)

				} catch (exc: Exception) {
					Log.e(TAG, "Use case binding failed", exc)
				}

			}, ContextCompat.getMainExecutor(requireContext())
		)

	}

	private fun stopCamera() {

	}


}