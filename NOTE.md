# Làm việc với `ReactiveCrudRepository` trong Reactive

## Cách hoạt động của method `save`

- Thực hiện lệnh `insert` nếu như object đó có giá trị = null ở field đánh annotation `@id`
- Thực hiện lệnh `update` nếu như object đó có giá trị != null ở field đánh annotation `@id`

Bản chất của vấn đề này là do `ReactiveCrudRepository` dùng `R2dbcEntityTemplate ` để thực hiện 2 lệnh `insert` và
`update` khi ta thực hiện lệnh `save` với `ReactiveCrudRepository`. `ReactiveCrudRepository` sẽ xem thử class để tạo ra
Object đó có giá trị != null ở field đánh annotation `@id` hay không mà đưa ra quyết định.

\* **Lưu ý:** Nếu muốn tạo giá trị cho field có đánh annotation `@id` thủ công thì *bỏ annotation `@id` ra khỏi class
đại diện cho object đó.* **Nhưng nếu làm điều đó thì lệnh `save` sẽ luôn thực hiện lệnh `insert`**.

```kotlin
@Table(name = "users")
class User : BaseEntity() {
    lateinit var phoneNumber: String // Đại diện làm `id` trong table database. Không thêm @id để tạo giá trị thủ công
    lateinit var password: String
    lateinit var email: String
}
```

Cách tốt nhất giải quyết vấn đề này là implement interface `Persistable<>`
```kotlin
@Table(name = "users")
class User : BaseEntity(), Persistable<String> {
    lateinit var phoneNumber: String // Đại diện làm `id` trong table database. Không thêm @id để tạo giá trị thủ công
    lateinit var password: String
    lateinit var email: String
    
    @Transient // Field này sẽ không được lưu vào database
    lateinit var  newProduct: Boolean // Field đại diện cho object này có phải mới hay không.

    // Method để cho `R2dbcEntityTemplate` phân biệt và thực hiện lệnh `insert` hay `save` cho đúng
    @Override
    @Transient
    fun  isNew(): Boolean {
        return this.newProduct || id == null
    }
}
```

# Cách hoạt động của `flatMap` trong `Mono` và `Flux`

- Nó sẽ thực sự thật hiện khi `Mono` hoặc `Flux` đó không bị **empty** và được _subscribe_ bởi ai đó bằng cách _return_ `Mono thực hiện lệnh flatMap`.