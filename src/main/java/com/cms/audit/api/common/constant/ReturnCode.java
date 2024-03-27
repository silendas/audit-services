package com.cms.audit.api.Common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {

    SUCESSFULLY_DEFAULT(200, "Proses Berhasil"),
    FAILED_DEFAULT(400, "Proses Gagal"),

    SUCCESSFULLY_LOGIN(200, "Login Berhasil"),
    SUCCESSFULLY_REGISTER(200, "Register Berhasil"),

    SUCCCESSFULLY_FIND_NIP(200, "Nip {0} Berhasil ditemukan"),
    SUCCESSFULLY_FIND_HISTORY(200, "{0} Berhasil menemukan history"),
    SUCCESSFULLY_ATTENDANCE_IN(200, "{0} Berhasil Clock In"),
    SUCCESSFULLY_ATTENDANCE_OUT(200, "{0} Berhasil Clock Out"),

    SUCCESSFULLY_INSERT(200, "Module {0} Berhasil Insert"),
    SUCCESSFULLY_UPDATE(200, "{0} Berhasil Update"),
    SUCCESSFULLY_DELETE(200, "{0} Berhasil Delete"),

    SUCCESSFULLY_APPROVED(200, "{0} Approval Berhasil"),
    SUCCESSFULLY_REJECTED(200, "{0} Rejected Berhasil"),
    SUCESSFULLY_DATA_FOUND(200, "Data Berhasil ditemukan"),

    SUCESSFULLY_LOGOUT(200, "Logout Berhasil"),


    FAILED_NIP_ALREADY_EXITS(302, "Nip {0} Telah Terdaftar"),
    FAILED_NIP_NOT_FOUND(404, "Nip {0} Tidak Terdaftar"),
    FAILED_INCORRECT_NIP_OR_PASS(300, "%s Gagal Nip atau Password "),
    FAILED_INSERT(305, "{0} Gagal Insert"),
    FAILED_UPDATE(306, "{0} Gagal Update"),
    FAILED_DELETE(307, "{0} Gagal Delete"),

    SUCCCESSFULLY_OTP(200, "OTP Sesuai"),
    FAILED_OTP_INCORRECT(300, "OTP Tidak Sesuai"),
    FAILED_OTP_EXPIRED(309, "OTP Telah Kadaluwarsa"),

    FAILED_NOT_FOUND(404, "{0} Data tidak ditemukan"),
    FAILED_BAD_REQUEST(400, "Request Tidak Sesuai"),
    FAILED_DATA_ALREADY_EXISTS(409, "Data duplicate"),
    FAILED_SERVER_INTERNAL_SERVER_ERROR(500, "Internal server error"),

    //User Management
    SUCCESSFULLY_ACCEPT_USER(200, "Izin Berhasil Diberikan"),
    SUCCESSFULLY_DECLINE_USER(200, "Berhasil"),

    // Logbook
    SUCCESSFULLY_GET_LOGBOOK(200, "Berhasil Mendapatkan Data Logbook"),
    SUCESSFULLY_ADD_LOGBOOK(200, "Berhasil Menambahkan Data Logbook"),
    SUCESSFULLY_APPROVE_LOGBOOK(200, "Berhasil Menyetujui Data Logbook"),
    SUCESSFULLY_MODIFY_LOGBOOK(200, "Berhasil Merubah Data Logbook"),

    // Submission
    SUCESSFULLY_GET_SUBMISSION_CATEGORY(200, "Berhasil Mendapatkan Data Kategori"),
    SUCESSFULLY_GET_SUBMISSION(200, "Berhasil Mendapatkan Data Persetujuan"),
    SUCESSFULLY_ADD_SUBMISSION(200, "Berhasil Menambahkan Data Persetujuan"),
    SUCESSFULLY_APPROVE_APPROVAL_SUBMISSION(200, "Berhasil Menyetujui Data Persetujuan"),
    SUCESSFULLY_UPDATE_APPROVAL_SUBMISSION(200, "Berhasil Memperbaharui Data Persetujuan"),


    //attandance
    SUCESSFULLY_UPDATE_APPROVAL_ATTANDANCE(200, "Berhasil Memperbaharui Data Persetujuan");


    // User Management

    private final int statusCode;
    private final String message;


}
