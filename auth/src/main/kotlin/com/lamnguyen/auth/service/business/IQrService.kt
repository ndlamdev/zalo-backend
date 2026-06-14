/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-30/09/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.responses.TokenResponse
import com.lamnguyen.auth.utils.enums.LoginStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Định nghĩa các nghiệp vụ đăng nhập bằng QR code.
 *
 * Service này quản lý vòng đời QR login, bao gồm tạo QR token, theo dõi trạng thái,
 * scan, xác nhận và đổi login token thành access token/refresh token.
 */
interface IQrService {
    /**
     * Tạo mã định danh phiên QR login và token dùng để render QR code.
     *
     * @return Mono chứa sid và QR scan token.
     */
    fun createQrCodeLoginAndToken(): Mono<Map<String, String>>

    /**
     * Mở luồng nhận sự kiện đăng nhập cho client đang hiển thị QR code.
     *
     * @param qrScanToken token được tạo khi khởi tạo QR login.
     * @return Flux phát ra trạng thái hoặc kết quả xác nhận đăng nhập.
     */
    fun subscribe(qrScanToken: String): Flux<String>

    /**
     * Xác nhận hoặc hủy phiên QR login sau khi đã scan.
     *
     * @param qrScanToken token của phiên QR login.
     * @param status trạng thái xác nhận, chỉ chấp nhận CONFIRM hoặc CANCELED.
     * @return Mono hoàn tất khi trạng thái được cập nhật.
     */
    fun confirm(qrScanToken: String, status: LoginStatus): Mono<Void>

    /**
     * Đánh dấu QR token đã được scan và chuyển phiên sang bước chờ xác nhận.
     *
     * @param qrScanToken token của phiên QR login.
     * @return Mono hoàn tất khi trạng thái scan được cập nhật.
     */
    fun scan(qrScanToken: String): Mono<Void>

    /**
     * Đăng nhập bằng login token được tạo sau khi QR login được xác nhận.
     *
     * @param loginToken token đăng nhập tạm thời.
     * @return Mono chứa access token và refresh token của người dùng.
     */
    fun login(loginToken: String?): Mono<TokenResponse>
}
