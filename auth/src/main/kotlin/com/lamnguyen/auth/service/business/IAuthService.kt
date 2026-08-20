package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.domain.responses.TokenResponse
import reactor.core.publisher.Mono

/**
 * Định nghĩa các nghiệp vụ xác thực chính của hệ thống.
 *
 * Service này xử lý vòng đời tài khoản và token, bao gồm đăng ký tài khoản,
 * kiểm tra số điện thoại, cấp lại token, đăng xuất và gửi mã OTP.
 */
interface IAuthService {
    /**
     * Đăng ký tài khoản mới bằng thông tin người dùng gửi lên.
     *
     * @param data thông tin đăng ký, bao gồm số điện thoại và mật khẩu.
     * @return Mono hoàn tất khi tài khoản được tạo và trạng thái đăng ký được lưu.
     */
    fun register(data: RegisterRequest): Mono<Void>

    /**
     * Kiểm tra số điện thoại đã tồn tại trong hệ thống hay chưa.
     *
     * @param phoneNumber số điện thoại cần kiểm tra.
     * @return Mono chứa true nếu số điện thoại đã tồn tại, false nếu chưa tồn tại.
     */
    fun existPhoneNumber(phoneNumber: String?): Mono<Boolean>

    /**
     * Cấp lại access token và refresh token từ refresh token hiện tại.
     *
     * @param refreshToken refresh token cần kiểm tra và thu hồi.
     * @return Mono chứa cặp token mới.
     */
    fun refresh(refreshToken: String): Mono<TokenResponse>

    /**
     * Đăng xuất người dùng bằng cách đưa refresh token và access token liên quan vào blacklist.
     *
     * @param refreshToken refresh token của phiên đăng nhập cần đăng xuất.
     * @return Mono hoàn tất khi token đã được thu hồi.
     */
    fun logout(refreshToken: String): Mono<Void>

    /**
     * Cấp lại token từ payload refresh token đã được xác thực trước đó.
     *
     * @param refreshToken Mono chứa payload của refresh token hợp lệ.
     * @return Mono chứa access token và refresh token mới.
     */
    fun refresh(refreshToken: Mono<RefreshTokenPayload>): Mono<TokenResponse>

    /**
     * Gửi mã OTP đến số điện thoại đã đăng ký thành công.
     *
     * @param phoneNumber số điện thoại nhận OTP.
     * @return Mono hoàn tất khi OTP được lưu cache và gửi qua Kafka.
     */
    fun sentOtp(phoneNumber: String): Mono<Void>

    /**
     * Xác thực OTP đăng ký tài khoản cho số điện thoại.
     *
     * @param phoneNumber số điện thoại đang muốn kiểm tra xác thực.
     * @param otp mã otp đã được gửi trước đã và sẽ dùng để xác thật.
     * @return Mono hoàn tất khi đã xác thật thành công, cập nhật thông tin account và xóa các trạng thái để có quyền cấp phát otp trước đó
     */
    fun validateAccount(phoneNumber: String, otp: String): Mono<Void>
}