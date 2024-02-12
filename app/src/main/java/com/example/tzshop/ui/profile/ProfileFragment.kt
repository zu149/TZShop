package com.example.tzshop.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tzshop.EntryActivity
import com.example.tzshop.FavouritesActivity
import com.example.tzshop.ProductPageActivity
import com.example.tzshop.data.users

import com.example.tzshop.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private val id = FirebaseAuth.getInstance().currentUser!!.uid.toString()
    private lateinit var id : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getSupportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        getSupportActionBar?.hide()

        id = getCurrentUserId()


        binding.button.setOnClickListener {
            clearFirestoreData()
            navigateToRegistrationScreen()

        }

        binding.favourites.setOnClickListener {
            val intent = Intent(requireActivity(), FavouritesActivity::class.java)
            startActivity(intent)
        }


        FirebaseFirestore.getInstance().collection("users")
            .document(id) //вызов функции получения айди юзера
            .get()

            .addOnSuccessListener { document ->
                val user = document.toObject(users::class.java)
                if ( user != null) {
                    binding.name.text = user.name
                    binding.surname.text = user.surname
                    binding.phone.text = user.phone
                }
            }


    }

    fun getCurrentUserId() : String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId

    }


    private fun clearFirestoreData() {
        FirebaseFirestore.getInstance().collection("users")
            .document(id)
            .delete()
            .addOnSuccessListener {

            }


    }

    private fun navigateToRegistrationScreen() {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), EntryActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}