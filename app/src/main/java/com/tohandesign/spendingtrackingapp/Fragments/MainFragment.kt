package com.tohandesign.spendingtrackingapp.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.tohandesign.spendingtrackingapp.Currency.CurrencyConverter
import com.tohandesign.spendingtrackingapp.Database.SpendingListAdapter
import com.tohandesign.spendingtrackingapp.Database.SpendingViewModel
import com.tohandesign.spendingtrackingapp.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment(), View.OnClickListener {

    val PREFS_FILENAME = "com.tohandesign.spendingtrackingapp"
    val KEY_NAME = "NAME"
    val KEY_GENDER = "GENDER"
    lateinit var itemView: View

    private lateinit var mSpendingViewModel: SpendingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        itemView = inflater.inflate(R.layout.fragment_main, container, false)

        setName()
        setRecyclerView("TRY")

        itemView.tlBtn.setOnClickListener(this)
        itemView.euroBtn.setOnClickListener(this)
        itemView.dolarBtn.setOnClickListener(this)
        itemView.sterlinBtn.setOnClickListener(this)

        return itemView
    }

    private fun setRecyclerView(base: String) {


        val adapter = SpendingListAdapter(requireContext(), base)
        val recyclerview = itemView.recyclerview
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        val currencyConverter = CurrencyConverter(requireContext())


        mSpendingViewModel = ViewModelProvider(this).get(SpendingViewModel::class.java)
        mSpendingViewModel.readAllData.observe(requireActivity(), Observer { spending ->
            adapter.setData(spending)
            var value: Double = 0.0;
            for (spent in spending) {
                value += currencyConverter.convert(spent.currency, base, spent.cost)

            }
            itemView.SpendingCost.setText(String.format("%.2f", value) + " " + base)
        })

        itemView.add_fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }
        itemView.btnPurchase.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<PurchaseFragment>(R.id.bgLayOutFragment).addToBackStack("PurchaseFragment")
            }
        }
    }


    private fun setName() {
        val prefences = requireContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val nameText: TextView = itemView.nameText
        if (prefences.getInt(KEY_GENDER, 0) == 1) {
            nameText.text = "Mr. " + prefences.getString(KEY_NAME, "A")
        } else if (prefences.getInt(KEY_GENDER, 0) == 2) {
            nameText.text = "Mrs. " + prefences.getString(KEY_NAME, "B")
        } else {
            nameText.text = prefences.getString(KEY_NAME, "A")
        }
        nameText.setOnClickListener { setNameDialog() }
    }

    private fun setNameDialog() {
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.change_name_dialog, null)
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView).show()

        val prefences = requireContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefences.edit()

        val nameText: TextInputEditText = mDialogView.findViewById(R.id.edittext)
        nameText.setText(prefences.getString(KEY_NAME, "Yasin"))
        val radioGroup: RadioGroup = mDialogView.findViewById(R.id.radioGroup)

        mDialogView.findViewById<Button>(R.id.saveButton).setOnClickListener {
            editor.putString(KEY_NAME, nameText.text.toString())
            if (radioGroup.checkedRadioButtonId == R.id.male)
                editor.putInt(KEY_GENDER, 1)
            else if (radioGroup.checkedRadioButtonId == R.id.female)
                editor.putInt(KEY_GENDER, 2)
            else
                editor.putInt(KEY_GENDER, 0)
            editor.apply()

            mBuilder.dismiss()
            setName()
        }
        mDialogView.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            mBuilder.dismiss()

        }
    }


    companion object {
        private const val NEW_NOTE_ACTIVITY_REQUEST_CODE = 1
    }

    fun changeBase(view: View) {
        when (view.id) {
            R.id.tlBtn -> {
                tlBtn.setTextColor(resources.getColor(R.color.custom_rose))
                sterlinBtn.setTextColor(resources.getColor(R.color.custom_charch))
                dolarBtn.setTextColor(resources.getColor(R.color.custom_charch))
                euroBtn.setTextColor(resources.getColor(R.color.custom_charch))
                setRecyclerView("TRY")
            }
            R.id.sterlinBtn -> {
                tlBtn.setTextColor(resources.getColor(R.color.custom_charch))
                sterlinBtn.setTextColor(resources.getColor(R.color.custom_rose))
                dolarBtn.setTextColor(resources.getColor(R.color.custom_charch))
                euroBtn.setTextColor(resources.getColor(R.color.custom_charch))
                setRecyclerView("GBP")
            }
            R.id.dolarBtn -> {
                tlBtn.setTextColor(resources.getColor(R.color.custom_charch))
                sterlinBtn.setTextColor(resources.getColor(R.color.custom_charch))
                dolarBtn.setTextColor(resources.getColor(R.color.custom_rose))
                euroBtn.setTextColor(resources.getColor(R.color.custom_charch))
                setRecyclerView("USD")
            }
            R.id.euroBtn -> {
                tlBtn.setTextColor(resources.getColor(R.color.custom_charch))
                sterlinBtn.setTextColor(resources.getColor(R.color.custom_charch))
                dolarBtn.setTextColor(resources.getColor(R.color.custom_charch))
                euroBtn.setTextColor(resources.getColor(R.color.custom_rose))
                setRecyclerView("EUR")
            }

        }

    }

    override fun onClick(view: View?) {
        if (view != null) {
            changeBase(view)
        }

    }


}