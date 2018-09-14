package baktiyar.com.poputkakg.ui.restore_password

interface RestorePasswordContract{
    interface View{
        fun onSuccessRestorePassword()
    }
    interface Presenter{
        fun restorePassword()
    }
}